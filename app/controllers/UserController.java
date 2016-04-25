package controllers;

import models.Comment;
import models.Tag;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.error404;
import views.html.externalUserView;
import views.html.userDashboard;
import views.html.userViews.followedTags;
import views.html.userViews.recentComments;
import views.html.userViews.userExerciseList;

import java.util.List;

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
                followedTags.apply(currentUser, currentUser.getTrackedTags()),
                recentComments.apply(Comment.getRecentComments(currentUser))
        ));
    }

    public Result edit(long userId) {
        return ok();
    }

    public Result renderUser(long userId) {
        User currentUser = SessionService.getCurrentUser();
        User viewedUser = User.find().byId(userId);
        if (viewedUser == null) {
            return notFound(error404.render(currentUser, "User not found!"));
        }
        List<Tag> tags = viewedUser.getTrackedTags();
        return ok(externalUserView.render(currentUser,
                viewedUser,
                followedTags.apply(viewedUser, tags),
                recentComments.apply(viewedUser.getComments()),
                userExerciseList.apply(viewedUser.getExercises())
        ));
    }
}
