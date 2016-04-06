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
     *
     * @return
     */
    public String getCurrentUserEmail(){
        return session(KEY_USERNAME);
    }

    public boolean isLoggedin(){
        return getCurrentUserEmail() != null;
    }

    public String get(String key){
        return session(key);
    }

    public User getCurrentUser(){
        return userRepository.find().where().eq("email", getCurrentUserEmail()).findUnique();
    }

    public void set(String value){
        session(KEY_USERNAME, value);
    }

    public void clear(){
        session().clear();
    }
}
