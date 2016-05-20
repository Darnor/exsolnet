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
import util.ValidationUtil;
import views.html.*;

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

    private static final String TITLE_FIELD = "title";
    private static final String EXERCISE_CONTENT_FIELD = "content";
    private static final String SOLUTION_CONTENT_FIELD = "contentsol";
    private static final String MAIN_TAG_FIELD = "maintag";
    private static final String OTHER_TAG_FIELD = "othertag";
    private static final String SOLUTION_COUNT_FIELD = "solutionCount";
    private static final String POINTS_FIELD = "points";
    private static final String TIME_FIELD = "time";
    private static final String OFFICIAL_FIELD = "isOfficial";
    private static final String TAG_NAME_DELIMITER = ",";
    private static final int PAGE_SIZE = 10;
    private static final int NO_OF_LATEST_SOLUTIONS = 2;
    private static final int NO_OF_TOP_SOLTUIONS = 1;
    private static final String FLASH_ERROR = "error";
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

    @Inject
    FormFactory formFactory;

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
     * Validite create of a exercise with a form.
     * title, maintag an content must be set.
     * @param title the title
     * @param mainTag the maintag
     * @param content the content
     */
    private static void validateFormData(String title, String mainTag, String content) {
        if (title.trim().isEmpty() || mainTag.trim().isEmpty() || ValidationUtil.isEmpty(content)) {
            throw new IllegalArgumentException("Formdata not valid.");
        }
    }

    /**
     * Validate create of a solution with a form.
     * @param solutionContent the content
     */
    private static void validateSolutionFormData(String solutionContent) {
        if (ValidationUtil.isEmpty(solutionContent)) {
            throw new IllegalArgumentException("Formdata not valid.");
        }
    }

    /**
     * All solutions that are marked as official
     * @param solutions the list of solutions
     * @return the list with only official solutions
     */
    private static List<Solution> getOfficialSolutions(List<Solution> solutions) {
        return solutions.stream().filter(Solution::isOfficial).collect(Collectors.toList());
    }

    /**
     * Get n solutions from list
     * @param solutions the list of solutions
     * @param n the amount of solutions that shall be returned
     * @return a new solution list with the first n elements from the given list
     */
    private static List<Solution> getFirstNoOfSolutions(List<Solution> solutions, int n) {
        return solutions.stream().limit(n).collect(Collectors.toList());
    }

    /**
     * A list of solutions sorted by time
     * @param solutions the unsorted solution list
     * @return the solutions sorted by time
     */
    private static List<Solution> getTimeSortedSolutions(List<Solution> solutions) {
        return solutions.stream()
                .sorted((s1, s2) -> s2.getTime().compareTo(s1.getTime()))
                .collect(Collectors.toList());
    }

    /**
     * A list of solutions sorted by points
     * @param solutions the unsorted solution list
     * @return the solutions sorted by time
     */
    private static List<Solution> getPointSortedSolutions(List<Solution> solutions) {
        return solutions.stream()
                .sorted((s1, s2) -> s1.getPoints() > s2.getPoints() ? -1 : s1.getPoints() < s2.getPoints() ? 1 : 0)
                .collect(Collectors.toList());
    }

    /**
     * deletes an exercise cascading!
     * checks if user is moderator or owner of exercise
     *
     * @param id id of to deleting exercise
     * @return ok if exercise has been deleted or unauthorized if user is not allowed to delete this exercise
     */
    public Result processDelete(long id) {
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator()) {
            try {
                Exercise.delete(id);
            } catch (IllegalArgumentException e) {
                Logger.error("Exercise was not found.", e);
                return notFound(error404.render(currentUser, "Die Aufgabe wurde nicht gefunden,"));
            }
            Logger.info("Exercise " + id + " deleted by " + currentUser.getEmail());
            flash("success", "Aufgabe gelöscht");
            flash("exercise_id", String.valueOf(id));
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
    public Result processUndo(long id) {
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator()) {
            try {
                Exercise.undoDelete(id);
            } catch (IllegalArgumentException e) {
                Logger.error("Exercise was not found.", e);
                return notFound(error404.render(currentUser, "Die Aufgabe wurde nicht gefunden,"));
            }
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

    /**
     * Get Exercise Data from Form and create or update the exercise
     * @param exerciseId the exercise id if the exercise should be updated
     */
    private void bindForm(Long exerciseId) {
        User currentUser = SessionService.getCurrentUser();
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String title = requestData.get(TITLE_FIELD);
        String content = ValidationUtil.sanitizeHtml(requestData.get(EXERCISE_CONTENT_FIELD));
        String mainTag = requestData.get(MAIN_TAG_FIELD);

        Logger.debug("Binding form with data: ");
        Logger.debug("Title: " + title);
        Logger.debug("Content: " + content);
        Logger.debug("mainTag: " + mainTag);

        try {
            validateFormData(title, mainTag, content);
        } catch (IllegalArgumentException e) {
            Logger.error("Exercise form data not valid");
            flash(FLASH_ERROR, "Eingegebene Aufgabendaten sind nicht vollständig.");
            throw e;
        }

        List<Tag> tags = Tag.createTagList(Tag.Type.MAIN, mainTag);

        String[] otherTags = request().body().asFormUrlEncoded().get(OTHER_TAG_FIELD);

        if (otherTags != null) {
            tags.addAll(Tag.createTagList(Tag.Type.NORMAL, otherTags));
        }

        if (exerciseId == null) {
            String solutionContent = ValidationUtil.sanitizeHtml(requestData.get(SOLUTION_CONTENT_FIELD));
            boolean isOfficial = "on".equals(requestData.get(OFFICIAL_FIELD));
            try {
                validateSolutionFormData(solutionContent);
            } catch (IllegalArgumentException e) {
                Logger.error("Created solution not valid");
                flash(FLASH_ERROR, "Lösung darf nicht leer sein.");
                throw e;
            }
            Exercise exercise = Exercise.create(title, content, tags, currentUser);
            Solution.create(solutionContent, exercise, currentUser, isOfficial);
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
        try {
            bindForm(null);
        } catch (IllegalArgumentException e) {
            Logger.error("Error when trying to bind the form", e);
            return redirect(routes.ExerciseController.renderCreate());
        }
        return redirect(routes.ExerciseController.renderOverview());
    }

    /**
     * Update an exercise based on the given exerciseId
     *
     * @param exerciseId the id of the exercise to be updated.
     * @return redirect to the exercise overview
     */
    public Result processUpdate(long exerciseId) {
        try {
            bindForm(exerciseId);
        } catch (IllegalArgumentException e) {
            Logger.error("Error when trying to bind the form", e);
            return redirect(routes.ExerciseController.renderEdit(exerciseId));
        }
        return redirect(routes.ExerciseController.renderDetail(exerciseId));
    }

    /**
     * Upvote a exercise
     * @param exerciseId the id of the exercise
     * @return amount of points
     */
    public Result processUpvote(long exerciseId) {
        Logger.info("Up Vote Exercise " + exerciseId);
        Exercise exercise = Exercise.findValidById(exerciseId);
        Vote.upvote(SessionService.getCurrentUser(), exercise);
        return ok(String.valueOf(Exercise.findValidById(exerciseId).getPoints()));
    }

    /**
     * Downvote a exercise
     * @param exerciseId the id of the exercise
     * @return amount of points
     */
    public Result processDownvote(long exerciseId) {
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
    public Result renderDetail(long id) {
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
    public Result renderSolutions(long exerciseId) {
        Exercise exercise = Exercise.findValidById(exerciseId);
        User currentUser = SessionService.getCurrentUser();

        List<Solution> solutions = getPointSortedSolutions(exercise.getValidSolutions());

        List<Solution> officialSolutions = getOfficialSolutions(solutions);
        solutions.removeAll(officialSolutions);

        List<Solution> topSolutions = getFirstNoOfSolutions(solutions, NO_OF_TOP_SOLTUIONS);
        solutions.removeAll(topSolutions);

        List<Solution> latestSolutions = getFirstNoOfSolutions(getTimeSortedSolutions(solutions), NO_OF_LATEST_SOLUTIONS);
        solutions.removeAll(latestSolutions);

        return ok(exerciseSolutions.render(
                currentUser,
                exercise,
                officialSolutions,
                topSolutions,
                latestSolutions,
                solutions
        ));
    }
}
