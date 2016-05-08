package controllers;

import com.avaje.ebean.PagedList;
import models.Exercise;
import models.Tag;
import models.User;
import models.Vote;
import models.builders.ExerciseBuilder;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.editExercise;
import views.html.error403;
import views.html.error404;
import views.html.exerciseList;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String MAIN_TAG_FIELD = "maintag";
    private static final String OTHER_TAG_FIELD = "othertag";
    private static final String SOLUTION_COUNT_FIELD = "solutionCount";
    private static final String POINTS_FIELD = "points";
    private static final String TIME_FIELD = "time";

    private static final String TAG_NAME_DELIMITER = ",";
    private static final int PAGE_SIZE = 10;

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

    private static void validateFormData(String title, String mainTag, String content) {
        if (title == null || mainTag == null || content == null) {
            throw new IllegalArgumentException("Formdata not valid. (null values)");
        }
        if (title.trim().length() == 0 || mainTag.trim().length() == 0 || content.trim().length() == 0) {
            throw new IllegalArgumentException("Formdata not valid. (empty values)");
        }
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
            return ok("Exercise deleted");
        }else{
            return unauthorized(error403.render(currentUser, "Keine Berechtigungen diese Aufgabe zu editieren"));
				}
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
        Exercise exercise = Exercise.findById(id);

        if (exercise == null) {
            return notFound(error404.render(currentUser, "Aufgabe nicht gefunden"));
        }

        if (!exercise.getUser().getId().equals(currentUser.getId())) {
            return unauthorized(error403.render(currentUser, "Keine Berechtigungen diese Aufgabe zu editieren"));
        }

        return ok(editExercise.render(currentUser, exercise, Tag.findTagsByType(Tag.Type.MAIN), Tag.findTagsByType(Tag.Type.NORMAL)));
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
            Exercise.create(title, content, tags, currentUser);
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
        Exercise exercise = Exercise.findById(exerciseId);
        Vote.upvote(SessionService.getCurrentUser(), exercise);
        return ok(String.valueOf(Exercise.findById(exerciseId).getPoints()));
    }

    public Result processDownvote(Long exerciseId) {
        Logger.info("Down Vote Exercise " + exerciseId);
        Exercise exercise = Exercise.findById(exerciseId);
        Vote.downvote(SessionService.getCurrentUser(), exercise);
        return ok(String.valueOf(Exercise.findById(exerciseId).getPoints()));
    }
}
