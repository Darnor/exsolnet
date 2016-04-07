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

    TagRepository tagRepository;

    public Result renderOverview() {
        return list(0, 1, "", "");
    }

    public Result renderCreate() {
        return ok(editExercise.render(Exercise.ExerciseBuilder.anExercise().withId(-1L).build(), sessionService.getCurrentUserEmail()));
    }

    public Result renderDetails(long id) {
        //TODO
        return notFound();
    }

    public Result list(int page, int order, String titleFilter, String tagFilter) {
        if (!sessionService.isLoggedin()) {
            return LoginController.redirectIfNotLoggedIn();
        }
        int pageSize = 5;
        String orderBy = exerciseRepository.getOrderByAttributeString(order);
        PagedList<Exercise> exercises = exerciseRepository.getPagedList(page, orderBy, titleFilter, tagFilter, pageSize);
        System.out.println(exercises.getDisplayXtoYofZ("a", "b"));
        return ok(exerciseList.render(exercises, order, titleFilter, tagFilter));
    }

    public Result edit(long id) {
        return ok(editExercise.render(exerciseRepository.find().byId(id), sessionService.getCurrentUserEmail()));
    }

    public Result update(long exerciseId) {
        DynamicForm requestData = formFactory.form().bindFromRequest();

        Exercise exercise;
        Long id;
        //create
        if (exerciseId == -1) {
            exercise = Exercise.ExerciseBuilder.anExercise().build();
            exerciseRepository.create(exercise);
            id = exercise.getId();
        } else {
            //edit
            id = exerciseId;
            exercise = exerciseRepository.findExerciseData(id);
        }


        exercise.setTitle(requestData.get("title"));
        exercise.setContent(requestData.get("content"));

        String maintagString = requestData.get("maintag");
        String othertagString = requestData.get("othertag");
        List<String> main = maintagString != null && !maintagString.equals("") ? Arrays.asList(maintagString.split(",")) : new ArrayList<String>();
        List<String> other = othertagString != null && !othertagString.equals("") ? Arrays.asList(othertagString.split(",")) : new ArrayList<String>();

        main.forEach(t -> {
                    if (!mainTagExistsInExercise(id, t)) {
                        Tag mainTag = getMainTagByName(t);

                        if (mainTag == null) {
                            throw new IllegalArgumentException("not allowed to create main tags.");
                        }
                        mainTag.addExercise(exercise);
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
                            tag.addExercise(exercise);
                            exercise.addTag(tag);
                            tagRepository.create(tag);
                        }
                        exercise.addTag(tag);
                    }
                }
        );

        exercise.getTags().forEach(t -> {
            if (main.contains(t.getName()) && !other.contains(t.getName())) {
                t.removeExercise(exercise.getId());
            }
        });
        exercise.getTags().removeIf(t -> !main.contains(t.getName()) && !other.contains(t.getName()));

        exerciseRepository.update(exercise);
        return redirect(routes.ExerciseController.renderOverview());
    }

    private Tag getOtherTagByName(String t) {
        try {
            Tag tag = tagRepository.findTagByName(t);
            if (tag == null || tag.isMainTag())
                return null;
            return tag;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private Tag getMainTagByName(String t) {

        try {
            Tag tag = tagRepository.findTagByName(t);
            if (tag == null || !tag.isMainTag())
                return null;
            return tag;
        } catch (NullPointerException e) {
            return null;
        }

    }

    private Boolean otherTagExistsInExercise(long id, String t) {
        try {

            List<Tag> tags = exerciseRepository.findExerciseData(id).getTags();
            for (Tag tag : tags) {
                if (tag.getName().equals(t) && !tag.isMainTag())
                    return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private Boolean mainTagExistsInExercise(long id, String t) {
        try {

            List<Tag> tags = exerciseRepository.findExerciseData(id).getTags();
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

