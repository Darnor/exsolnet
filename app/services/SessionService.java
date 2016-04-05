package services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.User;
import repositories.UserRepository;

import static play.mvc.Controller.session;

/**
 * Created by mario on 31.03.16.
 */
@Singleton
public class SessionService {

    public static final String KEY_USERNAME = "connected";

    @Inject
    private UserRepository userRepo;

    @Inject
    public SessionService(){
        //noop
    }

    public String getUsername(){
        return session(KEY_USERNAME);
    }

    public boolean isLoggedin(){
        return session(KEY_USERNAME) != null;
    }

    public String get(){
        return session(KEY_USERNAME);
    }

    public User getCurrentUser(){
        //TODO: use actual database when registration is implemented
        User fake = new User();
        fake.setEmail(getUsername());
        return fake;
    }

    public void set(String value){
        session(KEY_USERNAME, value);
    }

    public void clear(){
        session().clear();
    }
}
