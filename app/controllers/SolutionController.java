package controllers;

import models.Solution;
import models.User;
import models.Vote;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.error403;
import views.html.editSolution;

@Security.Authenticated(Secured.class)
public class SolutionController extends Controller {

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
            return redirect(routes.ExerciseDetailController.renderExerciseDetail(exercise_id));
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
            return redirect(routes.ExerciseDetailController.renderExerciseDetail(exercise_id));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen das Löschen dieser Lösung rückgängig zu machen"));
    }

    /**
     * renders an Formular with the Solution for edit
     *
     * @param SolutionId the id of the Solution
     * @return Result View of the detailed exercise with a edit solution formular.
     */
    public Result renderEditSolution(Long SolutionId) {
        Solution solution = Solution.findById(SolutionId);
        return ok(editSolution.render(SessionService.getCurrentUser(), solution.getExercise(),solution));
    }

}
