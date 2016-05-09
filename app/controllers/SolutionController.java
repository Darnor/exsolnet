package controllers;

import models.*;
import models.builders.SolutionBuilder;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.editSolution;
import views.html.error403;

import javax.inject.Inject;

@Security.Authenticated(Secured.class)
public class SolutionController extends Controller {

    private static final String CONTENT_FIELD = "content";

    @Inject
    FormFactory formFactory;

    public Result processUpvote(Long solutionId) {
        Logger.info("Up Vote Solution " + solutionId);
        Solution solution = Solution.findValidById(solutionId);
        Vote.upvote(SessionService.getCurrentUser(), solution);
        return ok(String.valueOf(Solution.findValidById(solutionId).getPoints()));
    }

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
        long exercise_id = Solution.findValidById(id).getExercise().getId();
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator() || currentUser.getId().equals(Solution.findValidById(id).getUser().getId())) {
            Solution.delete(id);
            Logger.info("Solution " + id + " deleted by " + currentUser.getEmail());
            flash("success", "Lösung gelöscht");
            flash("solution_id", "" + id);
            return redirect(routes.ExerciseController.renderDetail(exercise_id));
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
        long exercise_id = Solution.findById(id).getExercise().getId();
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator() || currentUser.getId().equals(Solution.findById(id).getUser().getId())) {
            Solution.undoDelete(id);
            Logger.info("Solution " + id + " undo deletion by " + currentUser.getEmail());
            return redirect(routes.ExerciseController.renderDetail(exercise_id));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen das Löschen dieser Lösung rückgängig zu machen"));
    }

    /**
     * renders an Formular with the Solution for edit
     *
     * @param SolutionId the id of the Solution
     * @return Result View of the detailed exercise with a edit solution formular.
     */
    public Result renderUpdate(Long SolutionId) {
        Solution solution = Solution.findById(SolutionId);
        return ok(editSolution.render(SessionService.getCurrentUser(), solution.getExercise(),solution));
    }

    /**
     * Create Exercise with new Solution
     *
     * @param exerciseId the exercise the solution is for
     * @return Result view of the exercise with all solutions and comments
     */
    public Result processCreate(Long exerciseId) {
        Solution.create(formFactory.form().bindFromRequest().get(CONTENT_FIELD), Exercise.findValidById(exerciseId), SessionService.getCurrentUser());
        return redirect(routes.ExerciseController.renderDetail(exerciseId));
    }

    /**
     * Update Exercise with new Solution
     *
     * @param solutionId the ID of the solution
     * @return Result view of the exercise with all solutions and comments
     */
    public Result processUpdate(Long solutionId) {
        Solution.update(solutionId, formFactory.form().bindFromRequest().get(CONTENT_FIELD));
        return redirect(routes.ExerciseController.renderDetail(Solution.findById(solutionId).getExercise().getId()));
    }

    /**
     * renders the exercise details with a create solution formular
     ** @param exerciseId the id of the Exercise
     * @return Result View of the detailed exercise with a create solution formular.
     */
    public Result renderCreate(long exerciseId) {
        return ok(editSolution.render(SessionService.getCurrentUser(), Exercise.findValidById(exerciseId), SolutionBuilder.aSolution().build()));
    }
}
