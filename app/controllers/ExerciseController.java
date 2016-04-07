package controllers;

import com.avaje.ebean.PagedList;

import models.Tag;
import play.data.DynamicForm;
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
public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    public Result renderOverview(){
        return list(0,1,"","");
    }

    public Result renderDetails(long id) {
        //TODO
        return notFound();
    }

    public Result list(int page, int order, String titleFilter, String tagFilter){
        if(!sessionService.isLoggedin()){
            return LoginController.redirectIfNotLoggedIn();
        }
        int pageSize = 5;
        String orderBy = Exercise.getOrderByAttributeString(order);
        PagedList<Exercise> exercises = Exercise.getPagedList(page,orderBy,titleFilter,tagFilter,pageSize);
        System.out.println(exercises.getDisplayXtoYofZ("a","b"));
        return ok(exerciseList.render(exercises,order,titleFilter,tagFilter));
    }

    public Result edit(long id) {
        return ok(editExercise.render(Exercise.find().byId(id), sessionService.getCurrentUserEmail()));
    }

    public Result update(long id) {
        //Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        DynamicForm requestData = formFactory.form().bindFromRequest();


        Exercise exercise = Exercise.findExerciseData(id);
        exercise.setTitle(requestData.get("title"));
        exercise.setContent(requestData.get("content"));

        List<String> main = Arrays.asList(requestData.get("maintag").split(","));
        List<String> other = Arrays.asList(requestData.get("othertag").split(","));


        main.forEach(t -> {
                    if (!mainTagExistsInExercise(id, t)) {
                        Tag mainTag = getMainTagByName(t);
                        if (mainTag == null) {
                            throw new IllegalArgumentException("not allowed to create main tags.");

                        }
                        exercise.addTag(mainTag);

                    }

                }
        );

        other.forEach(t -> {
                    if (!otherTagExistsInExercise(id, t)) {
                        Tag tag = getOtherTagByName(t);
                        if (tag == null) {
                            tag = new Tag();
                            tag.setMainTag(false);
                            tag.setName(t);

                            exercise.addTag(tag);
                            Tag.create(tag);
                        }

                        exercise.addTag(tag);

                    }
                }
        );

        exercise.getTags().removeIf(t -> !main.contains(t.getName()) && !other.contains(t.getName()));

        Exercise.update(exercise);
        return redirect(routes.ExerciseController.renderOverview());
    }
    
    private Tag getOtherTagByName(String t) {
        Tag tag = Tag.findTagByName(t);
        if (tag==null || tag.isMainTag()) return null;
        return tag;
    }

    private Tag getMainTagByName(String t) {
        Tag tag = Tag.findTagByName(t);
        if (tag==null || !tag.isMainTag()) return null;
        return tag;
    }

    private Boolean otherTagExistsInExercise(long id, String t) {

        List<Tag> tags = Exercise.findExerciseData(id).getTags();
        for (Tag tag : tags) {

            if (tag.getName().equals(t) && !tag.isMainTag()) {
                return true;
            }
        }
        return false;
    }

    private Boolean mainTagExistsInExercise(long id, String t) {
        List<Tag> tags = Exercise.findExerciseData(id).getTags();
        for (Tag tag : tags) {


            if (tag.getName().equals(t) && tag.isMainTag()) {
                return true;
            }
        }
        return false;
    }
}

