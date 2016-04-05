package controllers;

import com.avaje.ebean.PagedList;
import repositories.ExerciseRepository;
import models.Exercise;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.editExercise;
import views.html.exerciseList;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    ExerciseRepository exerciseRepository;

    public Result renderOverview(){
        return list(0,"title","");
    }

    public Result index() {
        return list(0,"title","");
    }

    public Result list(int page, String orderBy, String tagFilter){
        int pageSize = 5;
        PagedList<Exercise> exercises = exerciseRepository.getPagedList(page,orderBy,tagFilter,"",pageSize);
        return ok(exerciseList.render(exercises,orderBy,tagFilter));
    }

    public Result edit(long id) {
        if(session("connected") != null){
            return ok(editExercise.render(exerciseRepository.find().byId(id), session("connected")));
        }else{
            return ok(editExercise.render(exerciseRepository.find().byId(id), null));
        }
    }

    public Result update(long id) {
        Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        Exercise exercise = exerciseForm.bindFromRequest().get();
        exercise.setId(id);
        exerciseRepository.update(exercise);

        return redirect(routes.ExerciseController.list(0,"title",""));
        //return redirect(routes.HomeController.index());
    }
}
