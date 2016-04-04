package repositories;

import com.avaje.ebean.Model;
import com.google.inject.Singleton;
import models.User;

/**
 * Created by tourn on 4.4.16.
 */
@Singleton
public class UserRepository {

    private static final Model.Finder<Long, User> find = new Model.Finder<>(User.class);

    public Model.Finder<Long, User> find(){
        return find;
    }
}
