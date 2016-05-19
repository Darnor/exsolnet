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
import services.SessionService;
import util.ValidationUtil;
import views.html.editSolution;
import views.html.error403;
import views.html.error404;

import javax.inject.Inject;

@Security.Authenticated(Secured.class)
public class SolutionController extends Controller {

    private static final String CONTENT_FIELD = "content";
    private static final String OFFICIAL_FIELD = "isOfficial";


    @Inject
    FormFactory formFactory;

    /**
     * Upvote a solution
     * @param solutionId the solution id
     * @return the amount of points
     */
    public Result processUpvote(Long solutionId) {
        Logger.info("Up Vote Solution " + solutionId);
        Solution solution = Solution.findValidById(solutionId);
        Vote.upvote(SessionService.getCurrentUser(), solution);
        return ok(String.valueOf(Solution.findValidById(solutionId).getPoints()));
    }

    /**
     * Downvote a solution
     * @param solutionId the solution id
     * @return the amount of points
     */
    public Result processDownvote(Long solutionId) {
        Logger.info("Down Vote Solution " + solutionId);
        Solution solution = Solution.findValidById(solutionId);
        Vote.downvote(SessionService.getCurrentUser(), solution);
        return ok(String.valueOf(Solution.findValidById(solutionId).getPoints()));
    }

    /**
     * deletes a solution!
     *
     * @param id id of to deleting solution
     * @return ok if solution has been deleted or unauthorized if user is not allowed to delete this solution
     */
    public Result processDelete(Long id) {
        long exerciseId = Solution.findValidById(id).getExercise().getId();
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator() || currentUser.getId().equals(Solution.findValidById(id).getUser().getId())) {
            Solution.delete(id);
            Logger.info("Solution " + id + " deleted by " + currentUser.getEmail());
            flash("success", "Lösung gelöscht");
            flash("solution_id", "" + id);
            return redirect(routes.ExerciseController.renderDetail(exerciseId));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen diese Lösung löschen"));
    }

    /**
     * undo deletion of exercise
     *
     * @param id id of deleted exercise
     * @return
     */
    public Result processUndo(Long id) {
        User currentUser = SessionService.getCurrentUser();
        Solution solution = Solution.findById(id);

        if (solution == null) {
            return notFound(error404.render(currentUser, "Lösung nicht gefunden"));
        }

        long exerciseId = solution.getExercise().getId();

        if (currentUser.isModerator() || currentUser.getId().equals(Solution.findById(id).getUser().getId())) {
            Solution.undoDelete(id);
            Logger.info("Solution " + id + " undo deletion by " + currentUser.getEmail());
            return redirect(routes.ExerciseController.renderDetail(exerciseId));
        }

        return unauthorized(error403.render(currentUser, "Keine Berechtigungen das Löschen dieser Lösung rückgängig zu machen"));
    }

    /**
     * renders an Formular with the Solution for edit
     *
     * @param solutionId the id of the Solution
     * @return Result View of the detailed exercise with a edit solution formular.
     */
    public Result renderUpdate(Long solutionId) {
        Solution solution = Solution.findById(solutionId);
        return ok(editSolution.render(SessionService.getCurrentUser(), solution.getExercise(), solution));
    }

    /**
     * Create Exercise with new Solution
     *
     * @param exerciseId the exercise the solution is for
     * @return Result view of the exercise with all solutions and comments
     */
    public Result processCreate(Long exerciseId) {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        boolean isOfficial = "on".equals(requestData.get(OFFICIAL_FIELD));
        String content = requestData.get(CONTENT_FIELD);

        Logger.debug("Trying to create a new solution");
        if (!ValidationUtil.isEmpty(content)) {
            Logger.debug("Creating solution");
            Solution.create(ValidationUtil.sanitizeHtml(content), Exercise.findValidById(exerciseId), SessionService.getCurrentUser(), isOfficial);
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
        DynamicForm requestData = formFactory.form().bindFromRequest();
        boolean isOfficial = "on".equals(requestData.get(OFFICIAL_FIELD));
        String content = requestData.get(CONTENT_FIELD);

        Logger.debug("Trying to update a solution");
        if (!ValidationUtil.isEmpty(content)) {
            Logger.debug("Updating solution");
            Solution.update(solutionId, ValidationUtil.sanitizeHtml(content), isOfficial);
        }

        return redirect(routes.ExerciseController.renderDetail(Solution.findById(solutionId).getExercise().getId()));
    }

    /**
     * renders the exercise details with a create solution formular
     * * @param exerciseId the id of the Exercise
     *
     * @return Result View of the detailed exercise with a create solution formular.
     */
    public Result renderCreate(long exerciseId) {
        return ok(editSolution.render(SessionService.getCurrentUser(), Exercise.findValidById(exerciseId), SolutionBuilder.aSolution().build()));
    }
}
