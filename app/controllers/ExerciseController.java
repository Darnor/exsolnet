package controllers;

import models.Exercise;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.editExercise;
import views.html.exerciseList;
import views.html.index;

import javax.inject.Inject;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */

public class ExerciseController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result index() {
        return ok(exerciseList.render(0,Exercise.find.all(),"title","",5,Exercise.find.all().size()));
    }

    public Result list(int page, String orderBy, String filter){
        int pageSize = 5;
        java.util.List<Exercise> exercises = Exercise.find.where().contains("title",filter).orderBy(orderBy).findPagedList(page,pageSize).getList();
        return ok(exerciseList.render(page,exercises,orderBy,filter,pageSize,Exercise.find.all().size()));
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

        return redirect(routes.ExerciseController.list(0,"title",""));
        //return redirect(routes.HomeController.index());
    }
}
