package controllers;

import com.google.inject.Inject;
import models.User;
import models.builders.UserBuilder;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;
import views.html.editUser;
import views.html.login;

public class LoginController extends Controller {

    @Inject
    FormFactory formFactory;

    /**
     * Renders login and logout fields, depending if user is logged in
     * @return Result
     */
    public Result renderLogin() {
        return SessionService.getCurrentUser() != null ? redirect(routes.UserController.renderDashboard()) : ok(login.render(SessionService.getCurrentUser()));
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
        String name = requestData.get("emailorusername");
        String password = requestData.get("password");
        User user = User.authenticate(name, password);
        if (user == null) {
            Logger.warn(name + " tried to log in.");
            return redirect(routes.LoginController.renderLogin());
        }
        SessionService.set(user.getEmail());
        Logger.info(user.getEmail() + " is logged in");
        return redirect(routes.UserController.renderDashboard());
    }

    public Result renderRegister() {
        return ok(editUser.render(UserBuilder.anUser().withUsername("").withEmail("").build()));
    }

    public Result processRegister() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String username = requestData.get("username");
        String email = requestData.get("email");
        String password = requestData.get("password");
        String passwordCheck = requestData.get("password-check");
        if(UserController.validateUserForm(username, email, password, passwordCheck)) {
            User user = User.create(username, email, password, false);
            Logger.info("New user with name " + user.getUsername() + " just registered.");
            SessionService.set(user.getEmail());
            return redirect(routes.UserController.renderDashboard());
        }
        return redirect(routes.LoginController.renderLogin());
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
