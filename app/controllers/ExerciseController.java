package controllers;

import com.avaje.ebean.PagedList;
import models.*;
import models.builders.ExerciseBuilder;
import models.builders.SolutionBuilder;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.*;
import views.html.exerciseViews.exerciseSolutionList;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    private static final String TITLE_FIELD = "title";
    private static final String CONTENT_FIELD = "content";
    private static final String SOLUTION_CONTENT_FIELD = "contentsol";
    private static final String MAIN_TAG_FIELD = "maintag";
    private static final String OTHER_TAG_FIELD = "othertag";
    private static final String SOLUTION_COUNT_FIELD = "solutionCount";
    private static final String POINTS_FIELD = "points";
    private static final String TIME_FIELD = "time";

    private static final String TAG_NAME_DELIMITER = ",";
    private static final int PAGE_SIZE = 10;

    private static final int NO_OF_LATEST_SOLUTIONS = 2;
    private static final int NO_OF_TOP_SOLTUIONS = 1;

    /**
     * Map the Id of the html exercise-table to their Model-Attribute-name
     */
    private static final Map<Integer, String> tableHeaderMap;

    static {
        tableHeaderMap = new HashMap<>();
        tableHeaderMap.put(1, TITLE_FIELD);
        tableHeaderMap.put(2, SOLUTION_COUNT_FIELD);
        tableHeaderMap.put(3, POINTS_FIELD);
        tableHeaderMap.put(4, TIME_FIELD);
    }

    /**
     * deletes an exercise cascading!
     * checks if user is moderator or owner of exercise
     *
     * @param id id of to deleting exercise
     * @return ok if exercise has been deleted or unauthorized if user is not allowed to delete this exercise
     */
    public Result processDelete(Long id) {
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator()) {
            Exercise.delete(id);
            Logger.info("Exercise " + id + " deleted by " + currentUser.getEmail());
            flash("success", "Aufgabe gelöscht");
            flash("exercise_id", "" + id);
            return redirect(routes.ExerciseController.renderOverview());
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen diese Aufgabe zu editieren"));
    }

    /**
     * undo deletion of exercise
     *
     * @param id id of deleted exercise
     * @return
     */
    public Result processUndo(Long id) {
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator()) {
            Exercise.undoDelete(id);
            Logger.info("Exercise " + id + " undo deletion by " + currentUser.getEmail());
            return redirect(routes.ExerciseController.renderDetail(id));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen das Löschen dieser Aufgabe rückgängig zu machen"));
    }

    /**
     * render if create Exercise. Creates new blank exercise with id -1.
     * id -1 is important. And passes session of current user.
     *
     * @return Result of new Exercise
     */
    public Result renderCreate() {
        return ok(editExercise.render(
                SessionService.getCurrentUser(),
                ExerciseBuilder.anExercise().build(),
                SolutionBuilder.aSolution().build(),
                Tag.findTagsByType(Tag.Type.MAIN),
                Tag.findTagsByType(Tag.Type.NORMAL)
        ));
    }

    /**
     * render the Default ExerciseOverview View
     *
     * @return Result of the Default ExerciseOverview View
     */
    public Result renderOverview() {
        return renderList(0, 1, "", "");
    }

    /**
     * Converts the order-Id to the orderBy string
     *
     * @param order the orderID from the HTML-table
     * @return the order-by-attribute-string
     */
    static String getOrderByAttributeString(int order) {
        String result = tableHeaderMap.get(Math.abs(order));
        return order < 0 ? result + " desc" : result;
    }

    /**
     * render the ExerciseOverview View with given Parameters
     *
     * @param page        the Pagenumber of the list
     * @param order       the id of the Table-Header
     * @param titleFilter string which the title of an exercise should contains
     * @param tagFilter   string of tags for filter splittet by ','
     * @return Result View of the filtered ExerciseList
     */
    public Result renderList(int page, int order, String titleFilter, String tagFilter) {
        String orderBy = getOrderByAttributeString(order);
        PagedList<Exercise> exercises = Exercise.getPagedList(page, orderBy, titleFilter, tagFilter.split(TAG_NAME_DELIMITER), PAGE_SIZE);
        return ok(exerciseList.render(SessionService.getCurrentUser(), exercises, order, titleFilter, tagFilter));
    }

    /**
     * shows exercise with given id
     *
     * @param id exercise id that exists in db
     * @return redered exercise
     */
    public Result renderEdit(long id) {
        User currentUser = SessionService.getCurrentUser();
        Exercise exercise = Exercise.findValidById(id);

        if (exercise == null) {
            return notFound(error404.render(currentUser, "Aufgabe nicht gefunden"));
        }

        if (exercise.getUser().getId().equals(currentUser.getId()) || currentUser.isModerator()) {
            return ok(editExercise.render(currentUser, exercise, null, Tag.findTagsByType(Tag.Type.MAIN), Tag.findTagsByType(Tag.Type.NORMAL)));
        }
        return unauthorized(error403.render(currentUser, "Keine Berechtigungen diese Aufgabe zu editieren"));
    }

    private static void validateFormData(String title, String mainTag, String content) {
        if (title == null || mainTag == null || content == null) {
            throw new IllegalArgumentException("Formdata not valid. (null values)");
        }
        if (title.trim().length() == 0 || mainTag.trim().length() == 0 || content.trim().length() == 0) {
            throw new IllegalArgumentException("Formdata not valid. (empty values)");
        }
    }

    private void bindForm(Long exerciseId) {
        User currentUser = SessionService.getCurrentUser();
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String title = requestData.get(TITLE_FIELD);
        String content = requestData.get(CONTENT_FIELD);
        String mainTag = requestData.get(MAIN_TAG_FIELD);

        validateFormData(title, mainTag, content);

        List<Tag> tags = Tag.createTagList(Tag.Type.MAIN, mainTag);

        String[] otherTags = request().body().asFormUrlEncoded().get(OTHER_TAG_FIELD);

        if (otherTags != null) {
            tags.addAll(Tag.createTagList(Tag.Type.NORMAL, otherTags));
        }

        if (exerciseId == null) {
            String solutionContent = requestData.get(SOLUTION_CONTENT_FIELD);
            Exercise exercise = Exercise.create(title, content, tags, currentUser);
            Solution.create(solutionContent,exercise,currentUser);
        } else {
            Exercise.update(exerciseId, title, content, tags, currentUser);
        }
    }

    /**
     * Create a new exercise from the Form Data
     *
     * @return redirect to the exercise overview
     */
    public Result processCreate() {
        bindForm(null);
        return redirect(routes.ExerciseController.renderOverview());
    }

    /**
     * Update an exercise based on the given exerciseId
     *
     * @param exerciseId the id of the exercise to be updated.
     * @return redirect to the exercise overview
     */
    public Result processUpdate(long exerciseId) {
        bindForm(exerciseId);
        return redirect(routes.ExerciseController.renderOverview());
    }

    public Result processUpvote(Long exerciseId) {
        Logger.info("Up Vote Exercise " + exerciseId);
        Exercise exercise = Exercise.findValidById(exerciseId);
        Vote.upvote(SessionService.getCurrentUser(), exercise);
        return ok(String.valueOf(Exercise.findValidById(exerciseId).getPoints()));
    }

    public Result processDownvote(Long exerciseId) {
        Logger.info("Down Vote Exercise " + exerciseId);
        Exercise exercise = Exercise.findValidById(exerciseId);
        Vote.downvote(SessionService.getCurrentUser(), exercise);
        return ok(String.valueOf(Exercise.findValidById(exerciseId).getPoints()));
    }

    /**
     * renders the exercise Details. If the user has solved the exercise, renders additional all soultions with
     * comments. If the user hasn't solved the exercise, renders an create solution formular.
     *
     * @param id the id of the Exercise
     * @return Result View of the Detailed Exercise with Solution Formular or SolutionsList if the User has already
     * solved the Exercise
     */
    public Result renderDetail(Long id) {
        User user = SessionService.getCurrentUser();
        Exercise exercise = Exercise.findValidById(id);
        if (exercise != null) {
            return (user.hasSolved(id) || user.isModerator()) ? renderSolutions(id) : renderExerciseNotSolved(id);
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
        return ok(exerciseNotSolved.render(SessionService.getCurrentUser(), Exercise.findValidById(exerciseId)));
    }

    /**
     * renders the exercise details with all solutions and comments
     *
     * @param exerciseId the id of the Exercise
     * @return Result view of the exercise with all solutions and comments.
     */
    public Result renderSolutions(Long exerciseId) {
        Exercise exercise = Exercise.findValidById(exerciseId);
        User currentUser = SessionService.getCurrentUser();

        List<Solution> solutions = getPointSortedSolutions(exercise.getSolutions());

        List<Solution> officialSolutions = getOfficialSolutions(solutions);
        solutions.removeAll(officialSolutions);

        List<Solution> topSolutions = getFirstNoOfSolutions(solutions, NO_OF_TOP_SOLTUIONS);
        solutions.removeAll(topSolutions);

        List<Solution> latestSolutions = getFirstNoOfSolutions(getTimeSortedSolutions(solutions), NO_OF_LATEST_SOLUTIONS);
        solutions.removeAll(latestSolutions);

        return ok(exerciseSolutions.render(
                currentUser,
                exercise,
                exerciseSolutionList.apply(officialSolutions, currentUser, exerciseId),
                exerciseSolutionList.apply(topSolutions, currentUser, exerciseId),
                exerciseSolutionList.apply(latestSolutions, currentUser, exerciseId),
                exerciseSolutionList.apply(solutions, currentUser, exerciseId)
        ));
    }

    static List<Solution> getOfficialSolutions(List<Solution> solutions) {
        return solutions.stream().filter(Solution::isOfficial).collect(Collectors.toList());
    }

    static List<Solution> getFirstNoOfSolutions(List<Solution> solutions, int n) {
        return solutions.stream().limit(n).collect(Collectors.toList());
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
}
