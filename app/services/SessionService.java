package services;

import com.google.inject.Inject;

import static play.mvc.Controller.session;

/**
 * Created by mario on 31.03.16.
 */
public class SessionService {

    public static final String KEY_USERNAME = "connected";

    @Inject
    public SessionService(){

    }

    public String getUsername(){
        return session(KEY_USERNAME);
    }

    public String get(String key){
        return session(key);
    }

    public void set(String key, String value){
        session(key, value);
    }

    public void clear(){
        session().clear();
    }
}
