package controllers;

import models.Exercise;
import models.Solution;
import models.User;
import models.builders.SolutionBuilder;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.editSolution;
import views.html.error404;
import views.html.exerciseSolutions;

import javax.inject.Inject;

@Security.Authenticated(Secured.class)
public class ExerciseDetailController extends Controller {

    private static final String CONTENT_FIELD = "content";

    @Inject
    FormFactory formFactory;

    /**
     * renders the exercise Details. If the user has solved the exercise, renders additional all soultions with
     * comments. If the user hasn't solved the exercise, renders an create solution formular.
     *
     * @param id the id of the Exercise
     * @return Result View of the Detailed Exercise with Solution Formular or SolutionsList if the User has already
     * solved the Exercise
     */
    public Result renderExerciseDetail(Long id) {
        User user = SessionService.getCurrentUser();
        Exercise exercise = Exercise.findById(id);
        if (exercise != null) {
            if (!user.hasSolved(id)) {
                return renderCreateSolution(id);
            } else {
                return renderSolutions(id);
            }
        } else {
            return notFound(error404.render(user, "Diese Aufgabe existiert nicht"));
        }
    }

    /**
     * renders the exercise details with a create solution formular
     *
     * @param exerciseId the id of the Exercise
     * @return Result View of the detailed exercise with a create solution formular.
     */
    public Result renderCreateSolution(long exerciseId) {
        return ok(editSolution.render(SessionService.getCurrentUser(), Exercise.findById(exerciseId), SolutionBuilder.aSolution().build()));
    }

    /**
     * renders the exercise details with all solutions and comments
     *
     * @param exerciseId the id of the Exercise
     * @return Result view of the exercise with all solutions and comments.
     */
    public Result renderSolutions(Long exerciseId) {
        return ok(exerciseSolutions.render(SessionService.getCurrentUser(), Exercise.findById(exerciseId)));
    }

    /**
     * Update Exercise with new Solution
     *
     * @param exerciseId the exercise the solution is for
     * @return Result view of the exercise with all solutions and comments
     */
    public Result processUpdate(Long exerciseId) {
        Solution.create(formFactory.form().bindFromRequest().get(CONTENT_FIELD), Exercise.findById(exerciseId), SessionService.getCurrentUser());
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(exerciseId));
    }

    public Result upVote(Long solutionId) {
        Logger.info("Up Vote "+solutionId);

        Solution solution = Solution.findById(solutionId);
        solution.upvote(SessionService.getCurrentUser());
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(solution.getExercise().getId()));
    }
    public Result downVote(Long solutionId) {
        Logger.info("Down Vote "+solutionId);

        Solution solution = Solution.findById(solutionId);
        solution.downvote(SessionService.getCurrentUser());
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(solution.getExercise().getId()));
    }
}
