package controllers;

import com.avaje.ebean.PagedList;
import play.mvc.Results;
import repositories.ExerciseRepository;
import models.Exercise;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import services.SessionService;
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
    SessionService sessionService;

    @Inject
    ExerciseRepository exerciseRepository;

    public Result renderOverview(){
        return list(0,"title","","");
    }

    public Result renderDetails(long id){
        //TODO
        return notFound();
    }

    public Result list(int page, String orderBy, String titleFilter, String tagFilter){
        if(!sessionService.isLoggedin()){
            return LoginController.redirectIfNotLoggedIn();
        }
        int pageSize = 5;
        PagedList<Exercise> exercises = exerciseRepository.getPagedList(page,orderBy,titleFilter,tagFilter,pageSize);
        return ok(exerciseList.render(exercises,orderBy,titleFilter,tagFilter));
    }

    public Result edit(long id) {
            return ok(editExercise.render(exerciseRepository.find().byId(id), sessionService.getUsername()));
    }

    public Result update(long id) {
        Form<Exercise> exerciseForm = formFactory.form(Exercise.class);
        Exercise exercise = exerciseForm.bindFromRequest().get();
        exercise.setId(id);
        exerciseRepository.update(exercise);
        return redirect(routes.ExerciseController.renderOverview());
    }
}
