package services;

import models.User;

import static play.mvc.Controller.session;

public class SessionService {

    public static final String KEY_USEREMAIL = "connected";

    private SessionService() {}

    /**
     * returns the email of current user session
     * @return String email address
     */
    public static String getCurrentUserEmail(){
        return session(KEY_USEREMAIL);
    }

    /**
     * @return the user object of current user, null if no user is logged in
     */
    public static User getCurrentUser(){
        return User.find().where().ieq("email", getCurrentUserEmail()).findUnique();
    }

    /**
     *  sets a key value pair into session hashmap, key "connected" stores the current loggedin User's email
     * @param email adress of the user
     */
    public static void createSession(String email){
        session(KEY_USEREMAIL, email);
    }

    /**
     * clear's the session hashmap, user will be logged out
     */
    public static void clear(){
        session().clear();
    }
}
