package controllers;

import com.avaje.ebean.PagedList;

import com.avaje.ebean.enhance.agent.SysoutMessageOutput;
import models.Tag;
import play.data.DynamicForm;
import repositories.ExerciseRepository;
import models.Exercise;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import repositories.TagRepository;
import services.SessionService;
import views.html.editExercise;
import views.html.exerciseList;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    SessionService sessionService;

    @Inject
    ExerciseRepository exerciseRepository;


    @Inject
    TagRepository tagRepository;

    public Result renderOverview() {
        return list(0, "title", "", "");
    }

    public Result renderDetails(long id) {
        //TODO
        return notFound();
    }


    public Result list(int page, String orderBy, String titleFilter, String tagFilter) {
        if (!sessionService.isLoggedin()) {
            return LoginController.redirectIfNotLoggedIn();
        }
        int pageSize = 5;
        PagedList<Exercise> exercises = exerciseRepository.getPagedList(page, orderBy, titleFilter, tagFilter, pageSize);
        return ok(exerciseList.render(exercises, orderBy, titleFilter, tagFilter));
    }

    public Result edit(long id) {
        return ok(editExercise.render(exerciseRepository.find().byId(id), sessionService.getCurrentUserEmail()));
    }

    public Result update(long id) {
        //Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        DynamicForm requestData = formFactory.form().bindFromRequest();


        Exercise exercise = exerciseRepository.findExerciseData(id);
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
                            tagRepository.create(tag);
                        }

                        exercise.addTag(tag);

                    }
                }
        );

        exercise.getTags().removeIf(t -> !main.contains(t.getName()) && !other.contains(t.getName()));

        exerciseRepository.update(exercise);
        return redirect(routes.ExerciseController.renderOverview());
    }

    private Tag getOtherTagByName(String t) {
        Tag tag = tagRepository.findTagByName(t);
        if (tag.isMainTag()) return null;
        return tag;
    }

    private Tag getMainTagByName(String t) {
        Tag tag = tagRepository.findTagByName(t);
        if (!tag.isMainTag()) return null;
        return tag;
    }

    private Boolean otherTagExistsInExercise(long id, String t) {

        List<Tag> tags = exerciseRepository.findExerciseData(id).getTags();
        for (Tag tag : tags) {

            if (tag.getName().equals(t) && !tag.isMainTag()) {
                return true;
            }
        }
        return false;
    }

    private Boolean mainTagExistsInExercise(long id, String t) {
        List<Tag> tags = exerciseRepository.findExerciseData(id).getTags();
        for (Tag tag : tags) {


            if (tag.getName().equals(t) && tag.isMainTag()) {
                return true;
            }
        }
        return false;
    }
}

