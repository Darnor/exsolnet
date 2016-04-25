package controllers;

import models.Comment;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.userDashboard;
import views.html.userViews.followedTags;
import views.html.userViews.recentComments;

/**
 * Controller for DashboardView
 */
@Security.Authenticated(Secured.class)
public class UserController extends Controller {

    /**
     * Render the user dashboard route
     * @return the result
     */
    public Result renderDashboard() {
        User currentUser = SessionService.getCurrentUser();
        return ok(userDashboard.render(
                currentUser,
                followedTags.render(currentUser, currentUser.getTrackedTags()),
                recentComments.render(Comment.getRecentComments(currentUser))
        ));
    }

    public Result edit(long userId) {
        return ok();
    }

    public Result renderUser(long userId) {
        return ok();
    }
}
