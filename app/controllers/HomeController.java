package controllers;

import models.Exercise;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;
import java.sql.Date;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {


    @Inject
    FormFactory formFactory;

    public Result index() {
        if(session("connected") != null){
            return ok(index.render(Exercise.find.all(), session("connected")));
        }else{
            return ok(index.render(Exercise.find.all(), null));
        }
    }

    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();
        session("connected", user.getEmail());
        return redirect(routes.HomeController.index());
    }

    public Result logout() {
        session().clear();
        return redirect(routes.HomeController.index());
    }

    public Result addExercise() {
        Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        Exercise exercise = exerciseForm.bindFromRequest().get();
        exercise.setTime(new Date(System.currentTimeMillis()));
        Exercise.create(exercise);
        return redirect(routes.HomeController.index());
    }

}
