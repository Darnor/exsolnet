package controllers;

import com.google.inject.Inject;
import models.User;
import models.builders.UserBuilder;
import play.Configuration;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;
import views.html.editUser;
import views.html.info;
import views.html.login;

public class LoginController extends Controller {

    private static final String FLASH_ERROR = "error";

    @Inject
    FormFactory formFactory;

    @Inject
    Configuration configuration;

    /**
     * Renders login and logout fields, depending if user is logged in
     *
     * @return Result
     */
    public Result renderLogin() {
        return SessionService.getCurrentUser() != null ? redirect(routes.UserController.renderDashboard()) : ok(login.render(SessionService.getCurrentUser()));
    }

    /**
     * Render info page
     * @return Result of info page
     */
    public Result renderInfo() {
        return ok(info.render(SessionService.getCurrentUser()));
    }

    /**
     * Parses the body and puts including Form values into User Object
     * Authenticates the user based on email and password.
     * Sets the session on the user's email
     * redirect to the user dashboard
     *
     * @return Result
     */
    public Result processLogin() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String name = requestData.get("emailorusername");
        String password = requestData.get("password");
        User user = User.authenticate(name, password);
        if (user == null) {
            Logger.warn(name + " failed to log in.");
            flash(FLASH_ERROR, "Falscher Benutzername oder Passwort");
            return redirect(routes.LoginController.renderLogin());
        }

        SessionService.createSession(user.getEmail());
        Logger.debug(user.getEmail() + " is logged in");
        return redirect(routes.UserController.renderDashboard());
    }

    /**
     * Render Register, create a new user
     * @param username the username of the user
     * @param email the email of the user
     * @return Result
     */
    public Result renderRegister(String username, String email) {
        return ok(editUser.render(UserBuilder.anUser().withUsername(username).withEmail(email).build(), getRestrictedDomain()));
    }

    /**
     * Show error for registration validation
     * @param email the email adress of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param passwordCheck the password Check (password input second time) of the user
     */
    private void setFlashError(String email, String username, String password, String passwordCheck) {
        if (User.findByEmail(email) != null || User.findByUsername(username) != null) {
            flash(FLASH_ERROR, "Benutzer existiert bereits");
        }

        if (username == null || username.trim().isEmpty()) {
            flash(FLASH_ERROR, "Benutzername darf nicht leer sein.");
        }

        if (password == null || password.trim().isEmpty()) {
            flash(FLASH_ERROR, "Passwort darf nicht leer sein.");
        }

        if (!password.equals(passwordCheck)) {
            flash(FLASH_ERROR, "Passwort stimmt nicht überein.");
        }

        if (email == null || !UserController.validateEmailFormat(email)) {
            flash(FLASH_ERROR, "E-mailformat ist nicht korrekt.");
        }
    }

    private String getRestrictedDomain(){
        return configuration.getString("exsolnet.restrictedDomain", "");
    }

    /**
     * Register a user
     * @return Result, registration error or login
     */
    public Result processRegister() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String username = requestData.get("username");
        String originalEmail = requestData.get("email");
        String email = requestData.get("email") + getRestrictedDomain();
        String password = requestData.get("password");
        String passwordCheck = requestData.get("password-check");

        if (UserController.validateUserForm(username, password, passwordCheck) && UserController.validateEmailFormat(email) && User.findByEmail(email) == null && User.findByUsername(username) == null) {
            User user = User.create(username, email, password, false);
            Logger.debug("New user with name " + user.getUsername() + " and email " + user.getEmail() + " just registered.");
            SessionService.createSession(user.getEmail());
            return redirect(routes.UserController.renderDashboard());
        }

        Logger.debug("Registration failed. Reloading with username and email.");

        setFlashError(email, username, password, passwordCheck);

        return redirect(routes.LoginController.renderRegister(username, originalEmail));
    }

    /**
     * clears the current session
     * logout the user
     *
     * @return Result
     */
    public Result processLogout() {
        Logger.debug(SessionService.getCurrentUserEmail() + " just logged out.");
        SessionService.clear();
        return redirect(routes.UserController.renderDashboard());
    }
}
