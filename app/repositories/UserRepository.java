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

    /**
        Authenticates the user depending on email and password combination.
        Password is ignored for now, should in later stages be hashed and compared with value in database

        CARE: password not used in this implementation

        @param email Mail address of User, who wants to login
        @param password Password of User, who wants to login
     */
    public User authenticate(String email, String password){
        //TODO: use actual database when registration is implemented
        User fake = new User();
        fake.setEmail(email);
        return fake;
        //return find.where().eq("email", email).findUnique();
    }

    public Model.Finder<Long, User> find(){
        return find;
    }
}
