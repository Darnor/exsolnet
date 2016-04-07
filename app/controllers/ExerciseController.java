package controllers;

import com.avaje.ebean.PagedList;

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
    TagRepository  tagRepository;

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
                    if (getMainTagByName(id, t) == null) {

                        Tag tag = new Tag();
                        tag.setMainTag(true);
                        tag.setName(t);

                        exercise.addTag(tag);
                        tagRepository.create(tag);

                    } else if (getOtherTagByName(id, t) != null) {
                        exercise.getTags().forEach(t2 -> {
                            if (t2.getName().equals(t)) {
                                t2.setMainTag(true);
                            }
                        });
                    }
                }
        );

        other.forEach(t -> {

                    if (getOtherTagByName(id, t) == null) {

                        Tag tag = new Tag();
                        tag.setMainTag(false);
                        tag.setName(t);
                        exercise.addTag(tag);
                        tagRepository.create(tag);

                    } else {
                        if (getMainTagByName(id, t) != null) {
                            exercise.getTags().forEach(t2 -> {
                                if (t2.getName().equals(t)) {
                                    t2.setMainTag(false);
                                }
                            });
                        }
                    }
                }
        );

        exercise.getTags().removeIf(t -> !main.contains(t.getName()) && !other.contains(t.getName()));

        exerciseRepository.update(exercise);
        return redirect(routes.ExerciseController.renderOverview());
    }

    private Tag getOtherTagByName(long id, String t) {
        List<Tag> tags = exerciseRepository.findExerciseData(id).getTags();
        for (Tag tag : tags) {


            if (tag.getName().equals(t) && !tag.isMainTag()) {
                return tag;
            }
        }
        return null;
    }

    private Tag getMainTagByName(long id, String t) {
        List<Tag> tags = exerciseRepository.findExerciseData(id).getTags();
        for (Tag tag : tags) {


            if (tag.getName().equals(t) && tag.isMainTag()) {
                return tag;
            }
        }
        return null;
    }
}

