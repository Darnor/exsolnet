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
import views.html.exerciseSolutions;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

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
    public Result renderSolutions(Long exerciseId){
        Exercise exercise = Exercise.findById(exerciseId);
        return ok(exerciseSolutions.render(SessionService.getCurrentUser(), exercise, getSortedSolutionList(exercise.getSolutions())));
    }



    /**
     * sorts (and copy) the solution-list of the exercise for the View.
     * 1. Official Solution (or best solution if it has no official solution)
     * 2. Best Solution (or second best if it has no official solution or it is the best solution)
     * 3. Rest (sorted after date)
     *
     * @param solutions which should be sorted
     * @return a sorted solution List (copy)
     */
    protected static List<Solution> getSortedSolutionList(List<Solution> solutions){
        List<Solution> sortedSolutions = new ArrayList<>(solutions);
        if(sortedSolutions.size()>2){
            sortedSolutions = sortWithTwoOrMoreElements(sortedSolutions);
        }else{
            sortedSolutions.sort((e1,e2) -> (int)e1.getPoints()-(int)e2.getPoints());
        }
        return sortedSolutions;
    }

    /**
     * Helper-Method for getSortedSolutionList.
     * Sorts the Solution-list if it has more than 2 elements
     *
     * @param sortedSolutions which should be sorted
     * @return a sorted solution List
     */
    private static List<Solution> sortWithTwoOrMoreElements(List<Solution> sortedSolutions){
        Solution officialSolution = sortedSolutions.stream().filter(s -> s.isOfficial()).findFirst().orElse(null);
        if(officialSolution != null){
            sortedSolutions = sortWithOfficialSolution(sortedSolutions,officialSolution);
        }else{
            sortedSolutions = sortWithoutOfficialSolution(sortedSolutions);
        }
        sortedSolutions = moveWorseSolutionToTheEnd(sortedSolutions);
        return sortedSolutions;
    }

    /**
     * Helper-Method for getSortedSolutionList.
     * Moves worse solution to the end of the list
     * Worst solution will be the last element
     *
     * @param sortedSolutions which will be edited
     * @return a sorted solution List
     */
    private static List<Solution> moveWorseSolutionToTheEnd(List<Solution> sortedSolutions){
        int worsePointsLimit = -3;
        List<Solution> worseSolution = sortedSolutions.stream().filter(s -> s.getPoints()<=worsePointsLimit).collect(Collectors.toList());
        sortedSolutions.removeAll(worseSolution);
        worseSolution.sort((e1,e2) -> (int)e2.getPoints()-(int)e1.getPoints());
        worseSolution.stream().forEach(s -> sortedSolutions.add(s));
        return sortedSolutions;
    }

    /**
     * Helper-Method for getSortedSolutionList.
     * Sorts the Solution-list if it has more than 2 elements and an official solution
     *
     * @param sortedSolutions which should be sorted
     * @return a sorted solution List
     */
    private static List<Solution> sortWithOfficialSolution(List<Solution> sortedSolutions, Solution officialSolution){
        sortedSolutions.remove(officialSolution);
        sortedSolutions.sort((e1,e2) -> (int)e2.getPoints()-(int)e1.getPoints());
        Solution bestSolution = sortedSolutions.remove(0);
        sortedSolutions.sort((e1,e2) -> e2.getTime().compareTo(e1.getTime()));
        sortedSolutions.add(0,bestSolution);
        sortedSolutions.add(0,officialSolution);
        return sortedSolutions;
    }

    /**
     * Helper-Method for getSortedSolutionList.
     * Sorts the Solution-list if it has more than 2 elements and no official solution
     *
     * @param sortedSolutions which should be sorted
     * @return a sorted solution List
     */
    private static List<Solution> sortWithoutOfficialSolution(List<Solution> sortedSolutions){
        sortedSolutions.sort((e1,e2) -> (int)e2.getPoints()-(int)e1.getPoints());
        Solution bestSolution = sortedSolutions.remove(0);
        Solution secondBestSolution = sortedSolutions.remove(0);
        sortedSolutions.sort((e1,e2) -> e2.getTime().compareTo(e1.getTime()));
        sortedSolutions.add(0,secondBestSolution);
        sortedSolutions.add(0,bestSolution);
        return sortedSolutions;
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
     *
     * Upvote Solution
     * @param solutionId
     * @return
     */
    public Result upVoteSolution(Long solutionId) {
        Logger.info("Up Vote "+solutionId);

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
        Logger.info("Down Vote "+solutionId);

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
        Logger.info("Up Vote "+exerciseId);

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
        Logger.info("Down Vote "+exerciseId);

        Exercise exercise = Exercise.findById(exerciseId);
        Vote.downvote(SessionService.getCurrentUser(),exercise);
        return redirect(routes.ExerciseDetailController.renderExerciseDetail(exerciseId));
    }
}
