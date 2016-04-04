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
    private static final Model.Finder<Long, Exercise> find = new Model.Finder<>(Exercise.class);

    @Inject
    public ExerciseRepository(){
        //noop
    }

    public void create(Exercise exercise){
        exercise.save();
    }

    public void update(Exercise exercise){
        exercise.update();
    }

    public Model.Finder<Long, Exercise> find(){
        return find;
    }

}
