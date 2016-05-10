package controllers;

import models.User;
import models.builders.UserBuilder;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.*;

import javax.inject.Inject;

/**
 * Controller for DashboardView
 */
@Security.Authenticated(Secured.class)
public class UserController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result renderDashboard() {
        return ok(userDashboard.render(SessionService.getCurrentUser()));
    }

    static boolean validateUserForm(String username, String email, String password, String passwordCheck) {
        return password.equals(passwordCheck)
                && username.trim().length() > 0
                && password.trim().length() > 0
                && email.contains("@");
    }

    public Result processUpdate(long userId) {
        User currentUser = SessionService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            return unauthorized(error403.render(currentUser, "Kein Zugriff auf dieses Benutzerkonto."));
        }

        DynamicForm requestData = formFactory.form().bindFromRequest();
        String username = requestData.get("username");
        String email = requestData.get("email");
        String password = requestData.get("password");
        String passwordCheck = requestData.get("password-check");
        if(validateUserForm(username, email, password, passwordCheck)) {
            User.update(userId, username, email, password, false);
            return redirect(routes.UserController.renderDashboard());
        }
        return redirect(routes.UserController.renderEdit());
    }

    public Result renderEdit() {
        User currentUser = SessionService.getCurrentUser();
        if (currentUser == null) {
            currentUser = UserBuilder.anUser().withUsername("").withEmail("").build();
        }
        Logger.info(currentUser.getEmail() + " tried to render the user edit form.");
        return ok(editUser.render(currentUser));
    }

    public Result renderUser(long userId) {
        User currentUser = SessionService.getCurrentUser();
        User viewedUser = User.findById(userId);
        if (currentUser == null || viewedUser == null) {
            return notFound(error404.render(currentUser, "User not found!"));
        }
        if (currentUser.getId().equals(userId)) {
            return redirect(routes.UserController.renderDashboard());
        }
        return ok(externalUserView.render(currentUser, viewedUser));
    }
}
