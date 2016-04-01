package controllers;

import models.Exercise;
import models.ExsolnetUser;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import repositories.ExerciseRepository;
import services.SessionService;
import views.html.*;

import javax.inject.Inject;
import java.sql.Date;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class HomeController extends Controller {

    @Inject
    SessionService sessionService;

    @Inject
    FormFactory formFactory;

    @Inject
    ExerciseRepository exerciseRepository;

    public HomeController(){

    }

    public HomeController(ExerciseRepository exerciseRepository, SessionService sessionService){
        this.sessionService = sessionService;
        this.exerciseRepository = exerciseRepository;
    }


    public Result index() {
            return ok(index.render(exerciseRepository.find.all(), sessionService.getSession("connected")));
    }

    public Result login() {
        Form<ExsolnetUser> userForm = formFactory.form(ExsolnetUser.class);
        ExsolnetUser user = userForm.bindFromRequest().get();
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
        exerciseRepository.create(exercise);

        return redirect(routes.HomeController.index());
    }
}
