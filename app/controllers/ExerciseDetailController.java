package controllers;

import models.Exercise;
import models.Solution;
import models.User;
import models.Vote;
import models.builders.SolutionBuilder;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.editSolution;
import views.html.error404;
import views.html.exerciseNotSolved;
import views.html.exerciseSolutions;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Security.Authenticated(Secured.class)
public class ExerciseDetailController extends Controller {

    private static final String CONTENT_FIELD = "content";
    private static final int NO_OF_LATEST_SOLUTIONS = 2;
    private static final int NO_OF_TOP_SOLTUIONS = 1;

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
            return user.hasSolved(id) ? renderSolutions(id) : renderExerciseNotSolved(id);
        }
        return notFound(error404.render(user, "Diese Aufgabe existiert nicht"));
    }

    /**
     * renders the exercise details with an Info (like you have to solve the exercise first before looking
     * at other solutions
     *
     * @param exerciseId the id of the Exercise
     * @return Result View of the detailed exercise with no Solution and an Info
     */
    public Result renderExerciseNotSolved(long exerciseId) {
        return ok(exerciseNotSolved.render(SessionService.getCurrentUser(), Exercise.findById(exerciseId)));
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
        Exercise exercise = Exercise.findById(exerciseId);
        List<Solution> solutions = getPointSortedSolutions(exercise.getSolutions());
        List<Solution> officialSolutions = getOfficialSolutions(solutions);
        solutions.removeAll(officialSolutions);
        List<Solution> topSolutions = getFirstNoOfSolutions(solutions, NO_OF_TOP_SOLTUIONS);
        solutions.removeAll(topSolutions);
        List<Solution> latestSolutions = getFirstNoOfSolutions(getTimeSortedSolutions(solutions), NO_OF_LATEST_SOLUTIONS);
        solutions.removeAll(latestSolutions);
        return ok(exerciseSolutions.render(SessionService.getCurrentUser(), exercise, officialSolutions, topSolutions, latestSolutions, solutions));
    }

    static List<Solution> getOfficialSolutions(List<Solution> solutions) {
        return solutions.stream().filter(Solution::isOfficial).collect(Collectors.toList());
    }

    static List<Solution> getFirstNoOfSolutions(List<Solution> solutions, int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> (solutions.size() > n) ? solutions.get(i) : null)
                .filter(s -> s != null)
                .collect(Collectors.toList());
    }

    static List<Solution> getTimeSortedSolutions(List<Solution> solutions) {
        return solutions.stream()
                .sorted((s1, s2) -> s2.getTime().compareTo(s1.getTime()))
                .collect(Collectors.toList());
    }

    static List<Solution> getPointSortedSolutions(List<Solution> solutions) {
        return solutions.stream()
                .sorted((s1, s2) -> s1.getPoints() > s2.getPoints() ? -1 : s1.getPoints() < s2.getPoints() ? 1 : 0)
                .collect(Collectors.toList());
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

    /**
     * Upvote Solution
     * @param solutionId
     * @return
     */
    public Result upVoteSolution(Long solutionId) {
        Logger.info("Up Vote " + solutionId);
        Solution solution = Solution.findById(solutionId);
        Vote.upvote(SessionService.getCurrentUser(),solution);
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(solution.getExercise().getId()));
    }

    /**
     * Downvote Solution
     * @param solutionId
     * @return
     */
    public Result downVoteSolution(Long solutionId) {
        Logger.info("Down Vote " + solutionId);
        Solution solution = Solution.findById(solutionId);
        Vote.downvote(SessionService.getCurrentUser(),solution);
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(solution.getExercise().getId()));
    }

    /**
     * Upvote Exercise
     * @param exerciseId
     * @return
     */
    public Result upVoteExercise(Long exerciseId) {
        Logger.info("Up Vote " + exerciseId);
        Exercise exercise = Exercise.findById(exerciseId);
        Vote.upvote(SessionService.getCurrentUser(),exercise);
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(exerciseId));
    }

    /**
     * DownVOte Exercise
     * @param exerciseId
     * @return
     */
    public Result downVoteExercise(Long exerciseId) {
        Logger.info("Down Vote " + exerciseId);
        Exercise exercise = Exercise.findById(exerciseId);
        Vote.downvote(SessionService.getCurrentUser(),exercise);
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(exerciseId));
    }
}
