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

    public Result renderOverview(){
        return list(0,1,"","");
    }

    public Result renderDetails(long id) {
        //TODO
        return notFound();
    }

    public Result list(int page, int order, String titleFilter, String tagFilter) {
        int pageSize = 5;
        String orderBy = Exercise.getOrderByAttributeString(order);
        PagedList<Exercise> exercises = Exercise.getPagedList(page,orderBy,titleFilter,tagFilter,pageSize);
        return ok(exerciseList.render(exercises,order,titleFilter,tagFilter));
    }

    /**
     * shows exercise with given id
     * @param id exercise id that exists in db
     * @return redered exercise
     */
    public Result edit(long id) {
        return ok(editExercise.render(Exercise.find().byId(id), SessionService.getCurrentUserEmail()));
    }

    /**
     * Updates an exercise.
     * If exercise does not exist it will be created.
     * @param exerciseId the id of the exercise to be updated. if -1 it will be created.
     * @return the result
     */
    public Result update(long exerciseId) {
        //getting data from form
        DynamicForm requestData = formFactory.form().bindFromRequest();

        Exercise exercise;
        Long id;

        //create
        if (exerciseId == -1) {
            exercise = ExerciseBuilder.anExercise().build();
            Exercise.create(exercise);
            id = exercise.getId();
        } else {
            //edit
            id = exerciseId;
            exercise = Exercise.findExerciseData(id);
        }

        exercise.setTitle(requestData.get("title"));
        exercise.setContent(requestData.get("content"));

        //extract tag information
        String maintagString = requestData.get("maintag");
        String othertagString = requestData.get("othertag");
        // create a list out of the string. delimiter = ,
        List<String> main = maintagString != null && !maintagString.equals("") ? Arrays.asList(maintagString.split(",")) : new ArrayList<String>();
        List<String> other = othertagString != null && !othertagString.equals("") ? Arrays.asList(othertagString.split(",")) : new ArrayList<String>();

        //save maintags
        main.forEach(t -> {
                    // maintag is not yet saves
                    if (!mainTagExistsInExercise(id, t)) {
                        // get maintag from db
                        Tag mainTag = getMainTagByName(t);
                        //main tag does not exist in db
                        if (mainTag == null) {
                            throw new IllegalArgumentException("not allowed to create main tags.");
                        }
                        // add maintag to exercise and exercise to maintag
                        mainTag.addExercise(exercise);
                        exercise.addTag(mainTag);

                    }
                }
        );
        //save normal tags
        other.forEach(t -> {
                    // tag is not yet in exercise
                    if (!otherTagExistsInExercise(id, t)) {
                        //get tag from db
                        Tag tag = getOtherTagByName(t);
                        //tag has to be created
                        if (tag == null) {
                            tag = new Tag();
                            tag.setMainTag(false);
                            tag.setName(t);
                            tag.addExercise(exercise);
                            Tag.create(tag);
                            exercise.addTag(tag);
                        }
                        //add tag to exercise and exercise to tag
                        tag.addExercise(exercise);
                        exercise.addTag(tag);
                    }
                }
        );
        //remove exercise out of tag list if tag no longer exists
        exercise.getTags().forEach(t -> {
            if (main.contains(t.getName()) && !other.contains(t.getName())) {
                t.removeExercise(exercise.getId());
            }
        });
        // delete all tags from exercise which no longer exist
        exercise.getTags().removeIf(t -> !main.contains(t.getName()) && !other.contains(t.getName()));

        Exercise.update(exercise);
        //redirect to exercise list
        return redirect(routes.ExerciseController.renderOverview());
    }

    /**
     * gets other tag from db by name
     * @param t the name
     * @return the tag or null if it does not exist
     */
    private Tag getOtherTagByName(String t) {
        try {
            Tag tag = Tag.findTagByName(t);
            if (tag == null || tag.isMainTag())
                return null;
            return tag;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * gets main tag from db by name
     * @param t the name
     * @return the maintag or null if it does not exist in db
     */
    private Tag getMainTagByName(String t) {
        try {
            Tag tag = Tag.findTagByName(t);
            if (tag == null || !tag.isMainTag())
                return null;
            return tag;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * tells if the normal tag already is saved in the current exercise in the db
     * @param id the exercise id
     * @param t the name of the tag
     * @return true if it exists, false if its not yet saved
     */
    private Boolean otherTagExistsInExercise(long id, String t) {
        try {

            List<Tag> tags = Exercise.findExerciseData(id).getTags();
            for (Tag tag : tags) {
                if (tag.getName().equals(t) && !tag.isMainTag()) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     *  tells if the maintag is saved in the current exercise in the db
     * @param id the id of the exercise
     * @param t the name of the id
     * @return true if the maintag exists, false if it doesnt exist
     */
    private Boolean mainTagExistsInExercise(long id, String t) {
        try {
            List<Tag> tags = Exercise.findExerciseData(id).getTags();
            for (Tag tag : tags) {
                if (tag.getName().equals(t) && tag.isMainTag())
                    return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }
}

