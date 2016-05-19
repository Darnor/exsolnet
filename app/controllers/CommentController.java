package controllers;

import models.Comment;
import models.Exercise;
import models.Solution;
import models.User;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.error403;
import views.html.error404;

import javax.inject.Inject;

@Security.Authenticated(Secured.class)
public class CommentController extends Controller {

    private static final String CONTENT_FIELD = "content";
    private static final String COMMENT_NOT_FOUND = "Der Kommentar konnte nicht gefunden werden.";

    @Inject
    FormFactory formFactory;

    /**
     * create a comment for a existing exercise
     * @param exerciseId the exercise the comment shall be for
     * @return Result for new detailview with comment or error page if not allowed
     */
    public Result processCreateExerciseComment(long exerciseId) {
        User currentUser = SessionService.getCurrentUser();
        Exercise exercise = Exercise.findById(exerciseId);

        if (exercise == null) {
            Logger.error("Error while creating a comment: Exercise not found.");
            return notFound(error404.render(currentUser, "Die zu kommentierende Aufgabe wurde nicht gefunden."));
        }

        Logger.debug("New comment created by user " + currentUser.getUsername());
        Comment.create(formFactory.form().bindFromRequest().get(CONTENT_FIELD), Exercise.findById(exerciseId), currentUser);
        return redirect(routes.ExerciseController.renderDetail(exerciseId));
    }

    /**
     * Create a comment for a existing solution
     * @param solutionId the solution id the comment shall be for
     * @return the result detail view with the new comment or an errorpage if not allowed
     */
    public Result processCreateSolutionComment(long solutionId) {
        Solution solution = Solution.findById(solutionId);
        User currentUser = SessionService.getCurrentUser();

        if (solution == null) {
            Logger.error("Error while creating a comment: Solution not found.");
            return notFound(error404.render(currentUser, "Die zu kommentierende Aufgabe wurde nicht gefunden."));
        }

        if (currentUser.isModerator() || currentUser.hasSolved(solution.getExercise().getId())) {
            Logger.debug("New comment created by user " + currentUser.getUsername());
            Comment.create(formFactory.form().bindFromRequest().get(CONTENT_FIELD), solution, currentUser);
            return redirect(routes.ExerciseController.renderDetail(solution.getExercise().getId()));
        }

        Logger.error(currentUser.getUsername() + " is not authorized to create comment for solutionId " + solution.getId());
        return unauthorized(error403.render(currentUser, "Keine Berechtigung diese Lösung zu kommentieren."));
    }

    /**
     * Update a existing comment
     * @param commentId the id of the comment to be updated
     * @return detail view with updated comment
     */
    public Result processUpdate(long commentId) {
        Comment comment = Comment.findValidById(commentId);
        User user = SessionService.getCurrentUser();

        if (comment == null) {
            return notFound(error404.render(user, COMMENT_NOT_FOUND));
        }

        if (user.isModerator() || comment.getUser().getId().equals(user.getId())) {
            Comment.updateContent(commentId, formFactory.form().bindFromRequest().get(CONTENT_FIELD));
            Logger.debug("Comment: " + commentId + " updated with content: " + formFactory.form().bindFromRequest().get(CONTENT_FIELD));
        } else {
            Logger.error("User: " + user.getId() + " tried to edit comment: " + commentId + " from user: " + comment.getUser().getId());
            return unauthorized(error403.render(user, "Keine Berechtigung diesen Kommentar zu bearbeiten."));
        }

        if (comment.getExercise() != null) {
            return redirect(routes.ExerciseController.renderDetail(comment.getExercise().getId()));
        } else if (comment.getSolution() != null) {
            return redirect(routes.ExerciseController.renderDetail(comment.getSolution().getExercise().getId()));
        }
        return notFound(error404.render(user, "Der zu bearbeitete Kommentar ist keiner Aufgabe oder Lösung zugewiesen"));
    }

    /**
     * deletes a comment!
     *
     * @param id id of to deleting comment
     * @return ok if comment has been deleted or unauthorized if user is not allowed to delete this comment
     */
    public Result processDelete(long id) {
        long exerciseId = 0;
        Comment comment = Comment.findValidById(id);
        User currentUser = SessionService.getCurrentUser();

        if (comment == null) {
            return notFound(error404.render(currentUser, COMMENT_NOT_FOUND));
        }

        if (comment.getExercise() != null) {
            exerciseId = comment.getExercise().getId();
        } else if (comment.getSolution() != null) {
            exerciseId = comment.getSolution().getExercise().getId();
        }


        if (currentUser.isModerator() || currentUser.getId().equals(Comment.findValidById(id).getUser().getId())) {
            Comment.delete(id);
            Logger.info("Comment " + id + " deleted by " + currentUser.getEmail());
            flash("success", "Kommentar gelöscht");
            flash("comment_id", "" + id);
            return redirect(routes.ExerciseController.renderDetail(exerciseId));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen diesen Kommentar löschen"));
    }

    /**
     * undo deletion of comment
     *
     * @param id id of deleted comment
     * @return ok if comment has been undo deleted or unauthorized if user is not allowed to undo delete this comment
     */
    public Result processUndo(long id) {
        User currentUser = SessionService.getCurrentUser();
        Comment comment = Comment.findById(id);

        long exerciseId = 0;

        if (comment == null) {
            return notFound(error404.render(currentUser, COMMENT_NOT_FOUND));
        }

        if (comment.getExercise() != null) {
            exerciseId = comment.getExercise().getId();
        } else if (comment.getSolution() != null) {
            exerciseId = comment.getSolution().getExercise().getId();
        }

        if (currentUser.isModerator() || currentUser.getId().equals(Comment.findById(id).getUser().getId())) {
            Comment.undoDelete(id);
            Logger.info("Comment " + id + " undo deletion by " + currentUser.getEmail());
            return redirect(routes.ExerciseController.renderDetail(exerciseId));
        }

        return unauthorized(error403.render(currentUser, "Keine Berechtigungen das Löschen dieses Kommentars rückgängig zu machen"));
    }
}
