package repositories;

import com.avaje.ebean.Model;
import com.google.inject.Singleton;
import models.User;

import static models.User.UserBuilder.anUser;

/**
 * Created by tourn on 4.4.16.
 */
@Singleton
public class UserRepository {

    private static final Model.Finder<Long, User> find = new Model.Finder<>(User.class);

    /**
     * Authenticates the user depending on email and password combination.
     * Password is ignored for now, should in later stages be hashed and compared with value in database
     * <p>
     * CARE: password not used in this implementation
     *
     * @param email    Mail address of User, who wants to login
     * @param password Password of User, who wants to login
     * @return user
     */
    public User authenticate(String email, String password) {
        User user = findUserWithThisEmail(email);
        if (user == null) {
            //create user if non existing
            user = anUser().withEmail(email).build();
            user.save();
        }
        return user;
    }

    /**
     * Queries database, and returns User holding given email address
     * @param email
     * @return User
     */
    public User findUserWithThisEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    /**
     * Return Finder Object for DB's queries.
     *
     * CARE: should not be used! may be removed on next version
     *
     * @return Finder for User Models
     */
    public Model.Finder<Long, User> find() {
        return find;
    }
}
