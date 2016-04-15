package controllers;

import models.Comment;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.dashboard;

/**
 * Controller for DashboardView
 */
@Security.Authenticated(Secured.class)
public class DashboardController extends Controller {

    /**
     * Render the dashboard route
     * @return the result
     */
    public Result renderDashboard(){
        User currentUser = SessionService.getCurrentUser();
        return ok(dashboard.render(currentUser, User.getTrackedTags(currentUser), Comment.getRecentComments(currentUser)));
    }
}
