package controllers;

import com.avaje.ebean.PagedList;

import models.Tag;
import models.builders.ExerciseBuilder;
import play.data.DynamicForm;
import play.mvc.Security;
import models.Exercise;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import services.SessionService;
import views.html.editExercise;
import views.html.exerciseList;

import javax.inject.Inject;
import java.util.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    /**
     * render if create Exercise. Creates new blank exercise with id -1.
     * id -1 is important. And passes session of current user.
     *
     * @return Result of new Exercise
     */
    public Result renderCreate() {
        return ok(editExercise.render(ExerciseBuilder.anExercise().withId(-1L).build(), SessionService.getCurrentUserEmail()));
    }

    public Result renderOverview() {
        return list(0, 1, "", "");
    }

    public Result renderDetails(long id) {
        //TODO
        return notFound();
    }

    public Result list(int page, int order, String titleFilter, String tagFilter) {
        int pageSize = 5;
        String orderBy = Exercise.getOrderByAttributeString(order);
        PagedList<Exercise> exercises = Exercise.getPagedList(page, orderBy, titleFilter, tagFilter.split(","), pageSize);
        return ok(exerciseList.render(exercises, order, titleFilter, tagFilter));
    }

    /**
     * shows exercise with given id
     *
     * @param id exercise id that exists in db
     * @return redered exercise
     */
    public Result edit(long id) {
        Exercise exercise = Exercise.find().byId(id);

        if (exercise == null)
            return notFound();

        return ok(editExercise.render(exercise, SessionService.getCurrentUserEmail()));
    }

    /**
     * Returns a exercise.
     * If id exists. Get exercise from db. If if -1 create new Exercise and return
     *
     * @param exerciseId the id of the exercise or -1 if new
     * @return the exercise
     */
    private Exercise getExerciseToUpdate(Long exerciseId) {
        Exercise exercise;

        //create
        if (exerciseId == -1) {
            exercise = ExerciseBuilder.anExercise().build();
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
                        exercise.bindTag(mainTag);
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
                        exercise.bindTag(Tag.getOtherTagByNameOrCreate(t));
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

        List<String> tags = new ArrayList<String>();
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
        return data != null && !data.equals("") ? Arrays.asList(data.split(delimeter)) : new ArrayList<String>();
    }

    /**
     * Set exercise data for update
     * @param exercise the exercise to be set
     * @param requestData the data from the form
     * @throws IllegalArgumentException
     */
    private void setExerciseDataFromUpdateView(Exercise exercise, DynamicForm requestData) throws IllegalArgumentException {
        exercise.setTitle(requestData.get("title"));
        exercise.setContent(requestData.get("content"));
        List<String> main = getListFromString(requestData.get("maintag"), ",");
        List<String> other = getListFromString(requestData.get("othertag"), ",");
        updateTagInExercise(exercise, main, other);
    }

    /**
     * Updates an exercise.
     * If exercise does not exist it will be created.
     *
     * @param exerciseId the id of the exercise to be updated. if -1 it will be created.
     * @return the result
     */
    public Result update(long exerciseId) {
        //getting data from form
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Exercise exercise = getExerciseToUpdate(exerciseId);
        setExerciseDataFromUpdateView(exercise, requestData);
        Exercise.update(exercise);
        //redirect to exercise list
        return redirect(routes.ExerciseController.renderOverview());
    }



}

