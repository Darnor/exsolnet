package controllers;
import com.google.inject.Inject;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;

public class Secured extends Security.Authenticator {

    @Inject
    SessionService sessionService;

    @Override
    public String getUsername(Context ctx) {
        return sessionService.getCurrentUserEmail();
    }
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.LoginController.renderLogin());
    }
}
