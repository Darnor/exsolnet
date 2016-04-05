package controllers;

import models.Exercise;
import models.User;
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
            if(!sessionService.isLoggedin()){
                return redirect(routes.LoginController.renderLogin());
            }
            return ok(index.render(exerciseRepository.find().all(), sessionService.get()));
    }

    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();
        sessionService.set(user.getEmail());
        return redirect(routes.HomeController.index());
    }

    public Result logout() {
        sessionService.clear();
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
