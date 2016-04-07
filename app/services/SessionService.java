package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.User;
import repositories.UserRepository;

import static play.mvc.Controller.session;

/**
 * Created by mario on 31.03.16.
 */
public class SessionService {

    public static final String KEY_USERNAME = "connected";

    @Inject
    private UserRepository userRepository;

    @Inject
    public SessionService(){
        //noop
    }

    /**
     * returns the email of current user session
     * @return String email address
     */
    public String getCurrentUserEmail(){
        return session(KEY_USERNAME);
    }

    /**
     * checks if the user is logged in
     * @return boolean
     */
    public boolean isLoggedin(){
        return getCurrentUserEmail() != null;
    }

    /**
     * Access the session hashmap, key "connected" will return loggedin User's email
     * @param key
     * @return String
     */
    public String get(String key){
        return session(key);
    }

    /**
     * returns the user object of current user, null if no user is logged in
     * @return
     */
    public User getCurrentUser(){
        if(!isLoggedin()){
            return null;
        }
        return userRepository.find().where().eq("email", getCurrentUserEmail()).findUnique();
    }

    /**
     *  sets a key value pair into session hashmap, key "connected" stores the current loggedin User's email
     * @param value
     */
    public void set(String value){
        session(KEY_USERNAME, value);
    }

    /**
     * clear's the session hashmap, user will be logged out
     */
    public void clear(){
        session().clear();
    }
}
