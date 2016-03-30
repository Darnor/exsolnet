package controllers;

import models.Exercise;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.editExercise;
import views.html.index;

import javax.inject.Inject;
import java.sql.Date;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result index() {
        if(session("connected") != null){
            return ok(index.render(Exercise.find.all(), session("connected")));
        }else{
            return ok(index.render(Exercise.find.all(), null));
        }
    }

    public Result edit(long id) {
        if(session("connected") != null){
            return ok(editExercise.render(Exercise.find.byId(id), session("connected")));
        }else{
            return ok(editExercise.render(Exercise.find.byId(id), null));
        }
    }

    public Result update(long id) {
        Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        Exercise exercise = exerciseForm.bindFromRequest().get();
        exercise.setId(id);
        Exercise.update(exercise);

        return redirect(routes.HomeController.index());
    }
}
