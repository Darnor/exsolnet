package repositories;

import com.avaje.ebean.Model;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Exercise;

/**
 * Created by mario on 31.03.16.
 */

@Singleton
public class ExerciseRepository {
    @Inject
    public ExerciseRepository(){

    }

    public void create(Exercise exercise){
        exercise.save();
    }

    public void update(Exercise exercise){
        exercise.update();
    }

    public static Model.Finder<Long, Exercise> find = new Model.Finder<Long,Exercise>(Exercise.class);
}
