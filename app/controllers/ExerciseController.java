package controllers;

import com.avaje.ebean.PagedList;
import models.Exercise;
import models.Tag;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static models.builders.ExerciseBuilder.anExercise;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    private static final String TITLE_STR = "title";
    private static final String CONTENT_STR = "content";
    private static final String MAIN_TAG_STR = "mainTag";
    private static final String OTHER_TAG_STR = "otherTag";

    /**
     * render if create Exercise. Creates new blank exercise with id -1.
     * id -1 is important. And passes session of current user.
     *
     * @return Result of new Exercise
     */
    public Result renderCreate() {
        return ok(editExercise.render(anExercise().build(), SessionService.getCurrentUserEmail()));
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
        return ok(exerciseDetail.render(Exercise.findExerciseData(id), SessionService.getCurrentUserEmail()));
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
        return ok(exerciseList.render(SessionService.getCurrentUserEmail(), exercises, order, titleFilter, tagFilter));
    }

    /**
     * shows exercise with given id
     *
     * @param id exercise id that exists in db
     * @return redered exercise
     */
    public Result edit(long id) {
        Exercise exercise = Exercise.find().byId(id);
        return (exercise == null) ? notFound(fileNotFound.render("Exercise Not Found")) : ok(editExercise.render(exercise, SessionService.getCurrentUserEmail()));
    }

    /**
     * Returns a exercise.
     * If id exists. Get exercise from db. If null create new Exercise and return
     *
     * @param exerciseId the id of the exercise or null if new
     * @return the exercise
     */
    private Exercise getExerciseToUpdate(Long exerciseId) {
        Exercise exercise;

        //create
        if (exerciseId == null) {
            exercise = anExercise().build();
            Exercise.create(exercise);
        } else {
            //get
            exercise = Exercise.findExerciseData(exerciseId);
        }
        return exercise;
    }


    private void saveMaintagInExercise(Exercise exercise, List<String> main) {
        main.forEach(t -> {
                    // maintag is not yet saves
                    if (!Exercise.mainTagExistsInExercise(exercise.getId(), t)) {
                        // get maintag from db
                        Tag mainTag =   Tag.findMainTagByName(t);

                        //main tag does not exist in db
                        if (mainTag == null) {
                            throw new IllegalArgumentException("not allowed to create main tags.");
                        }
                        // add maintag to exercise and exercise to maintag
                        Exercise.bindTag(exercise,mainTag);
                    }
                }
        );
    }

    private void saveOtherTagInExercise(Exercise exercise, List<String> other) {
        other.forEach(t -> {
                    if (Exercise.mainTagExistsInExercise(exercise.getId(), t))
                        throw new IllegalArgumentException("not allowed to add maintag to othertag.");

                    // tag is not yet in exercise
                    if (!Exercise.otherTagExistsInExercise(exercise.getId(), t)) {
                        //get tag from db or create and add tag to exercise and exercise to tag
                        Exercise.bindTag(exercise,Tag.getOtherTagByNameOrCreate(t));
                    }
                }
        );
    }

    /**
     * Updated tag in exercise by new tag list
     * @param exercise the exercise to be updated
     * @param main the main tags
     * @param other the other tags
     */
    private void updateTagInExercise(Exercise exercise, List<String> main, List<String> other) {
        saveMaintagInExercise(exercise, main);
        saveOtherTagInExercise(exercise, other);

        List<String> tags = new ArrayList<>();
        tags.addAll(main);
        tags.addAll(other);

        exercise.removeTagIfNotInList(tags);
    }

    /**
     * Greate a List from a String with delimeter
     * @param data the string
     * @param delimeter the delimeter
     * @return a list from a string or a new list if string is null
     */
    private List<String> getListFromString(String data, String delimeter) {
        return data != null && data.length() > 0 ? Arrays.asList(data.split(delimeter)) : new ArrayList<>();
    }

    /**
     * Set exercise data for update
     * @param exercise the exercise to be set
     * @param requestData the data from the form
     * @throws IllegalArgumentException
     */
    private void setExerciseDataFromUpdateView(Exercise exercise, DynamicForm requestData) {
        exercise.setTitle(requestData.get(TITLE_STR));
        exercise.setContent(requestData.get(CONTENT_STR));
        List<String> main = getListFromString(requestData.get(MAIN_TAG_STR), ",");
        List<String> other = getListFromString(requestData.get(OTHER_TAG_STR), ",");
        updateTagInExercise(exercise, main, other);
    }

    /**
     * Updates an exercise.
     *
     * @param exerciseId the id of the exercise to be updated. if -1 it will be created.
     * @return the result
     */
    public Result processUpdate(Long exerciseId) {
        //getting data from form
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Exercise exercise = getExerciseToUpdate(exerciseId);
        setExerciseDataFromUpdateView(exercise, requestData);
        Exercise.update(exercise);
        //redirect to exercise list
        return redirect(routes.ExerciseController.renderOverview());
    }

    /**
     * Creates an exercise
     *
     * @return the result
     */
    public Result processCreate() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Exercise exercise = anExercise().withUser(SessionService.getCurrentUser()).build();
        setExerciseDataFromUpdateView(exercise, requestData);
        exercise.save();
        return redirect(routes.ExerciseController.renderOverview());
    }
}
