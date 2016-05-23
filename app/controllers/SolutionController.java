package controllers;

import models.Exercise;
import models.Solution;
import models.User;
import models.Vote;
import models.builders.SolutionBuilder;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.SecurityUtil;
import views.html.editSolution;
import views.html.error403;
import views.html.error404;

import javax.inject.Inject;

import static models.Exercise.findValidById;
import static services.SessionService.getCurrentUser;

@Security.Authenticated(Secured.class)
public class SolutionController extends Controller {

    private static final String CONTENT_FIELD = "content";
    private static final String OFFICIAL_FIELD = "isOfficial";

    private static final String SOLUTION_NOT_FOUND = "Lösung nicht gefunden.";
    private static final String LOG_SOLUTION_NOT_FOUND = "Solution not found.";

    @Inject
    FormFactory formFactory;

    /**
     * Upvote a solution
     * @param solutionId the solution id
     * @return the amount of points
     */
    public Result processUpvote(long solutionId) {
        Logger.info("Up Vote Solution " + solutionId);
        User currentUser = getCurrentUser();
        Solution solution = Solution.findValidById(solutionId);

        if (solution == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(currentUser, SOLUTION_NOT_FOUND));
        }

        Vote.upvote(currentUser, solution);
        return ok(String.valueOf(Solution.findValidById(solutionId).getPoints()));
    }

    /**
     * Downvote a solution
     * @param solutionId the solution id
     * @return the amount of points
     */
    public Result processDownvote(long solutionId) {
        Logger.info("Down Vote Solution " + solutionId);

        User currentUser = getCurrentUser();
        Solution solution = Solution.findValidById(solutionId);

        if (solution == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(currentUser, SOLUTION_NOT_FOUND));
        }

        Vote.downvote(currentUser, solution);
        return ok(String.valueOf(Solution.findValidById(solutionId).getPoints()));
    }

    /**
     * deletes a solution!
     *
     * @param id id of to deleting solution
     * @return ok if solution has been deleted or unauthorized if user is not allowed to delete this solution
     */
    public Result processDelete(long id) {
        User currentUser = getCurrentUser();
        Solution solution = Solution.findValidById(id);

        if (solution == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(currentUser, SOLUTION_NOT_FOUND));
        }

        if (currentUser.isModerator() || currentUser.getId().equals(solution.getUser().getId())) {
            Solution.delete(id);
            Logger.info("Solution " + id + " deleted by " + currentUser.getEmail());
            flash("success", "Lösung gelöscht");
            flash("solution_id", String.valueOf(id));
            return redirect(routes.ExerciseController.renderDetail(solution.getExercise().getId()));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen diese Lösung löschen"));
    }

    /**
     * undo deletion of exercise
     *
     * @param id id of deleted exercise
     * @return
     */
    public Result processUndo(long id) {
        User currentUser = getCurrentUser();
        Solution solution = Solution.findById(id);

        if (solution == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(currentUser, SOLUTION_NOT_FOUND));
        }

        if (currentUser.isModerator() || currentUser.getId().equals(Solution.findById(id).getUser().getId())) {
            Solution.undoDelete(id);
            Logger.info("Solution " + id + " undo deletion by " + currentUser.getEmail());
            return redirect(routes.ExerciseController.renderDetail(solution.getExercise().getId()));
        }

        return unauthorized(error403.render(currentUser, "Keine Berechtigungen das Löschen dieser Lösung rückgängig zu machen"));
    }

    /**
     * renders an Formular with the Solution for edit
     *
     * @param solutionId the id of the Solution
     * @return Result View of the detailed exercise with a edit solution formular.
     */
    public Result renderUpdate(long solutionId) {
        User currentUser = getCurrentUser();
        Solution solution = Solution.findValidById(solutionId);

        if (solution == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(currentUser, SOLUTION_NOT_FOUND));
        }

        return ok(editSolution.render(currentUser, solution.getExercise(), solution));
    }

    /**
     * Create Exercise with new Solution
     *
     * @param exerciseId the exercise the solution is for
     * @return Result view of the exercise with all solutions and comments
     */
    public Result processCreate(long exerciseId) {
        User currentUser = getCurrentUser();
        Exercise exercise = findValidById(exerciseId);

        if (exercise == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(currentUser, "Aufgabe konnte nicht gefunden werden."));
        }

        DynamicForm requestData = formFactory.form().bindFromRequest();
        boolean isOfficial = "on".equals(requestData.get(OFFICIAL_FIELD));
        String content = SecurityUtil.sanitizeHtml(requestData.get(CONTENT_FIELD));

        Logger.debug("Trying to create a new solution");
        if (!SecurityUtil.isEmpty(content)) {
            Logger.debug("Creating solution");
            Solution.create(content, exercise, currentUser, isOfficial);
        }

        return redirect(routes.ExerciseController.renderDetail(exerciseId));
    }

    /**
     * Update Exercise with new Solution
     *
     * @param solutionId the ID of the solution
     * @return Result view of the exercise with all solutions and comments
     */
    public Result processUpdate(long solutionId) {
        Solution solution = Solution.findValidById(solutionId);

        if (solution == null) {
            Logger.error(LOG_SOLUTION_NOT_FOUND);
            return notFound(error404.render(getCurrentUser(), SOLUTION_NOT_FOUND));
        }

        DynamicForm requestData = formFactory.form().bindFromRequest();
        boolean isOfficial = "on".equals(requestData.get(OFFICIAL_FIELD));
        String content = SecurityUtil.sanitizeHtml(requestData.get(CONTENT_FIELD));

        Logger.debug("Trying to update a solution");
        if (!SecurityUtil.isEmpty(content)) {
            Logger.debug("Updating solution");
            Solution.update(solutionId, content, isOfficial);
        }

        return redirect(routes.ExerciseController.renderDetail(solution.getExercise().getId()));
    }

    /**
     * renders the exercise details with a create solution formular
     * * @param exerciseId the id of the Exercise
     *
     * @return Result View of the detailed exercise with a create solution formular.
     */
    public Result renderCreate(long exerciseId) {
        User currentUser = getCurrentUser();
        Exercise exercise = findValidById(exerciseId);

        if (exercise == null) {
            Logger.error("Exercise not found.");
            return notFound(error404.render(currentUser, "Aufgabe konnte nicht gefunden werden."));
        }

        return ok(editSolution.render(currentUser, exercise, SolutionBuilder.aSolution().build()));
    }
}
