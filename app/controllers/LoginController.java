package controllers;

import com.google.inject.Inject;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

public class LoginController extends Controller {

    @Inject
    FormFactory formFactory;

    /**
     * Renders login and logout fields, depending if user is logged in
     * @return Result
     */
    public Result renderLogin() {
        return SessionService.getCurrentUser() != null ? redirect(routes.UserController.renderDashboard()) : ok(views.html.login.render(SessionService.getCurrentUser()));
    }

    /**
     * Parses the body and puts including Form values into User Object
     * Authenticates the user based on email and password.
     * Sets the session on the user's email
     * redirect to the user dashboard
     * @return Result
     */
    public Result login() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        User user = User.authenticate(requestData.get("emailorusername"), requestData.get("password"));
        if(user != null){
            SessionService.set(user.getEmail());

            Logger.info(user.getEmail()+" is logged in");

        }
        return redirect(routes.UserController.renderDashboard());
    }

    /**
     * clears the current session
     * logout the user
     * @return Result
     */
    public Result logout() {
        if(SessionService.isLoggedin()){
            SessionService.clear();
        }
        return redirect(routes.UserController.renderDashboard());
    }
}
