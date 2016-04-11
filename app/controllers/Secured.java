package controllers;
import com.google.inject.Inject;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;

public class Secured extends Security.Authenticator {

    @Inject
    SessionService sessionService;

    /**
     * takes the pending requests context and checks if a user is logged in,
     * return User if logged in, else returns null
     * @param ctx
     * @return
     */
    @Override
    public String getUsername(Context ctx) {
        return sessionService.getCurrentUserEmail();
    }

    /**
     * if not logged in, this method redirect to given route
     * @param ctx
     * @return
     */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.LoginController.renderLogin());
    }
}
