package controllers;

import models.User;
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

    public static final String EMAIL_FORMAT = "^[0-9A-Za-z.\\-_]+@[0-9A-Za-z.\\-_]+\\.[0-9A-Za-z]{2,}$";

    @Inject
    FormFactory formFactory;

    /**
     * Validate the Email for correct Format
     * @param email the email to be validated
     * @return true if email is valide. false if email is invalid.
     */
    static boolean validateEmailFormat(String email) {
        return email.matches(EMAIL_FORMAT);
    }

    /**
     * Validate the User Form if userinput is valid
     * @param username the username
     * @param password the password
     * @param passwordCheck the second time the password is entered
     * @return true if valid input
     */
    static boolean validateUserForm(String username, String password, String passwordCheck) {
        return password.equals(passwordCheck)
                && password.trim().length() > 0
                && validateUsernameFormat(username);
    }

    private static boolean validateUsernameFormat(String username) {
        return !username.trim().isEmpty() && username.matches("[A-Za-z0-9_\\-]{4,}");
    }

    /**
     * Render the Dashboard for the current user
     * @return Result
     */
    public Result renderDashboard() {
        return ok(userDashboard.render(SessionService.getCurrentUser()));
    }

    /**
     * Update the information of the user
     * @param userId the user id to be updated
     * @return Result. Dashboard if valide. EditPage of nonvalid.
     */
    public Result processUpdate(long userId) {
        User currentUser = SessionService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            Logger.warn(currentUser.getEmail() + " tried to access an non-existing userId");
            return unauthorized(error403.render(currentUser, "Kein Zugriff auf dieses Benutzerkonto."));
        }

        DynamicForm requestData = formFactory.form().bindFromRequest();
        String username = requestData.get("username");
        String password = requestData.get("password");
        String passwordCheck = requestData.get("password-check");
        boolean isModerator = currentUser.isModerator();

        if (password.equals(passwordCheck) && password.isEmpty() && validateUsernameFormat(username)) {
            Logger.debug("Empty password, don't update it.");
            User.update(userId, username, null, isModerator);
            flash("success", "Benutzername wurde angepasst. Passwort ist immernoch das gleiche.");
            return redirect(routes.UserController.renderDashboard());
        }

        if (validateUserForm(username, password, passwordCheck)) {
            Logger.debug(currentUser.getUsername() + " is now known as " + username);
            User.update(userId, username, password, isModerator);
            Logger.debug("User updated.");
            flash("success", "Benutzername und/oder Passwort wurde angepasst.");
            return redirect(routes.UserController.renderDashboard());
        }

        Logger.error("New userdata could not be verified, redirecting to the edit page again.");
        flash("error", "Benutzerdaten konnten nicht angepasst werden.");
        return redirect(routes.UserController.renderEdit());
    }

    /**
     * Render Edit Page for the user profile
     * @return Result
     */
    public Result renderEdit() {
        User currentUser = SessionService.getCurrentUser();
        Logger.debug(currentUser.getEmail() + " wants to edit the profile");
        return ok(editUser.render(currentUser, ""));
    }

    /**
     * Render User Dashboard
     * @param userId the id of the user that will be rendered
     * @return Result. User Dashboard or Error Page if invalid.
     */
    public Result renderUser(long userId) {
        User currentUser = SessionService.getCurrentUser();
        User viewedUser = User.findById(userId);
        if (viewedUser == null) {
            Logger.error("Viewed user could not be found.");
            return notFound(error404.render(currentUser, "User not found!"));
        }
        if (currentUser.getId().equals(userId)) {
            Logger.debug(currentUser.getUsername() + " accessed the dashboard.");
            return redirect(routes.UserController.renderDashboard());
        }

        Logger.debug(currentUser.getUsername() + " accessed the dashboard of " + viewedUser.getUsername());
        return ok(externalUserView.render(currentUser, viewedUser));
    }
}
