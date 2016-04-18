package controllers;

import com.avaje.ebean.PagedList;
import models.Exercise;
import models.Tag;
import models.builders.ExerciseBuilder;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.editExercise;
import views.html.exerciseDetail;
import views.html.exerciseList;
import views.html.fileNotFound;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
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
    private static final String MAIN_TAG_FIELD = "mainTag";
    private static final String OTHER_TAG_FIELD = "otherTag";

    private static final String TAG_NAME_DELIMITER = ",";

    private static List<Tag> createTagList(String tagNames, boolean isMainTag) {
        return Arrays.asList(tagNames.split(TAG_NAME_DELIMITER)).stream()
                .map(String::trim)
                .filter(t -> t.length() > 0)
                .distinct()
                .map(s -> {
                    Tag tag = Tag.findTagByName(s);
                    return tag == null ? Tag.create(s, isMainTag) : tag;
                })
                .collect(Collectors.toList());
    }

    private static void validateFormData(String title, List<Tag> mainTags, String content) {
        if (title.trim().length() == 0 || mainTags.isEmpty() || content.trim().length() == 0) {
            throw new IllegalArgumentException("Formdata not valid.");
        }
    }

    /**
     * render if create Exercise. Creates new blank exercise with id -1.
     * id -1 is important. And passes session of current user.
     *
     * @return Result of new Exercise
     */
    public Result renderCreate() {
        return ok(editExercise.render(SessionService.getCurrentUser(), ExerciseBuilder.anExercise().build()));
    }

    /**
     * render the Default ExerciseOverview View
     *
     * @return Result of the Default ExerciseOverview View
     */
    public Result renderOverview() {
        return list(0, 1, "", "");
    }

    public Result renderDetails(Long id) {
        return ok(exerciseDetail.render(SessionService.getCurrentUser(), Exercise.findById(id)));
    }

    /**
     * render the ExerciseOverview View with given Parameters
     *
     * @param page the Pagenumber of the list
     * @param order the id of the Table-Header
     * @param titleFilter string which the title of an exercise should contains
     * @param tagFilter string of tags for filter splittet by ','
     * @return Result View of the filtered ExerciseList
     */
    public Result list(int page, int order, String titleFilter, String tagFilter) {
        int pageSize = 10;
        String orderBy = Exercise.getOrderByAttributeString(order);
        PagedList<Exercise> exercises = Exercise.getPagedList(page, orderBy, titleFilter, tagFilter.split(","), pageSize);
        return ok(exerciseList.render(SessionService.getCurrentUser(), exercises, order, titleFilter, tagFilter));
    }

    /**
     * shows exercise with given id
     *
     * @param id exercise id that exists in db
     * @return redered exercise
     */
    public Result edit(long id) {
        Exercise exercise = Exercise.find().byId(id);
        return (exercise == null) ? notFound(fileNotFound.render("Exercise Not Found")) : ok(editExercise.render(SessionService.getCurrentUser(), exercise));
    }

    public Result processCreate() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String title = requestData.get(TITLE_FIELD);
        String content = requestData.get(CONTENT_FIELD);
        List<Tag> tags = createTagList(requestData.get(MAIN_TAG_FIELD), true);

        validateFormData(title, tags, content);

        tags.addAll(createTagList(requestData.get(OTHER_TAG_FIELD), false));
        Exercise.create(title, content, tags, SessionService.getCurrentUser());
        return redirect(routes.ExerciseController.renderOverview());
    }

    /**
     * Update or create an exercise based on the
     *
     * @param exerciseId the id of the exercise to be updated.
     * @return the result
     */
    public Result processUpdate(long exerciseId) {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String title = requestData.get(TITLE_FIELD);
        String content = requestData.get(CONTENT_FIELD);
        List<Tag> tags = createTagList(requestData.get(MAIN_TAG_FIELD), true);

        validateFormData(title, tags, content);

        tags.addAll(createTagList(requestData.get(OTHER_TAG_FIELD), false));
        Exercise.update(exerciseId, title, content, tags, SessionService.getCurrentUser());
        return redirect(routes.ExerciseController.renderOverview());
    }
}
