package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

/**
 * Created by mario on 05.04.16.
 */
public class LoginController extends Controller {

    @Inject
    SessionService sessionService;

    @Inject
    FormFactory formFactory;

    /**
     * Renders login and logout fields, depending if user is logged in
     * @return Result
     */
    public Result renderLogin(){
            return ok(views.html.login.render(sessionService.getCurrentUserEmail()));
    }

    /**
     * Parses the body and puts including Form values into User Object
     * Authenticates the user based on email and password.
     * Sets the session on the user's email
     * redirect to dashboard
     * @return Result
     */
    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();
        if(User.authenticate(user.getEmail(), user.getPassword()) != null){
            sessionService.set(user.getEmail());
        }
        return redirect(routes.DashboardController.renderDashboard());
    }

    /**
     * clears the current session
     * logout the user
     * @return Result
     */
    public Result logout() {
        if(sessionService.isLoggedin()){
            sessionService.clear();
        }
        return redirect(routes.DashboardController.renderDashboard());
    }

    /**
     * static function which redirect to login screen
     * @return Result
     */
    public static Result redirectIfNotLoggedIn(){
        return redirect(routes.LoginController.renderLogin());
    }
}
