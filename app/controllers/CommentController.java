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

    @Inject
    FormFactory formFactory;

    public Result processCreateExerciseComment(Long exerciseId) {
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

    public Result processCreateSolutionComment(Long solutionId) {
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

    public Result processUpdate(Long commentId) {
        Comment comment = Comment.findById(commentId);
        User user = SessionService.getCurrentUser();

        if (comment == null) {
            return notFound(error404.render(user, "Der Kommentar konnte nicht gefunden werden."));
        }

        if (user.isModerator() || comment.getUser().getId().equals(user.getId())) {
            Comment.updateContent(commentId, formFactory.form().bindFromRequest().get(CONTENT_FIELD));
            Logger.debug("Comment: " + commentId + " updated with content: " + formFactory.form().bindFromRequest().get(CONTENT_FIELD));
        } else {
            Logger.error("User: " + user.getId() + " tried to edit comment: " + commentId + " from user: " + comment.getUser().getId());
            return unauthorized(error403.render(user, "Keine Berechtigung diesen Kommentar zu bearbeiten."));
        }

        if (comment.getExercise()!=null) {
            return redirect(routes.ExerciseController.renderDetail(comment.getExercise().getId()));
        } else if(comment.getSolution() != null) {
            return redirect(routes.ExerciseController.renderDetail(comment.getSolution().getExercise().getId()));
        }
        return notFound(error404.render(user,"Der zu bearbeitete Kommentar ist keiner Aufgabe oder Lösung zugewiesen"));
    }
}
