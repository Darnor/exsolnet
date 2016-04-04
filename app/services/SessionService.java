package services;

import com.google.inject.Inject;

import static play.mvc.Controller.session;

/**
 * Created by mario on 31.03.16.
 */
public class SessionService {

    @Inject
    public SessionService(){

    }

    public String getUsername(String key){
        return session(key);
    }
}
