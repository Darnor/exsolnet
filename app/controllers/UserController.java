package controllers;

import models.Comment;
import models.Tag;
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
import views.html.userViews.followedTags;
import views.html.userViews.recentComments;
import views.html.userViews.userExerciseList;
import views.html.userViews.userSolutionList;

import javax.inject.Inject;
import java.util.List;

/**
 * Controller for DashboardView
 */
@Security.Authenticated(Secured.class)
public class UserController extends Controller {

    @Inject
    FormFactory formFactory;

    public static boolean validateUserForm(String username, String email, String password, String passwordCheck) {
        return password.equals(passwordCheck)
                && username.trim().length() > 0
                && password.trim().length() > 0
                && email.contains("@");
   }

    /**
     * Render the user dashboard route
     * @return the result
     */
    public Result renderDashboard() {
        User currentUser = SessionService.getCurrentUser();
        return ok(userDashboard.render(
                currentUser,
                followedTags.apply(currentUser, currentUser.getTrackedTags()),
                recentComments.apply(Comment.getRecentComments(currentUser)),
                userExerciseList.apply(currentUser.getExercises()),
                userSolutionList.apply(currentUser.getSolutions())
        ));
    }

    public Result processUpdate(long userId) {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String username = requestData.get("username");
        String email = requestData.get("email");
        String password = requestData.get("password");
        String passwordCheck = requestData.get("password-check");
        if(validateUserForm(username, email, password, passwordCheck)) {
            User.update(userId, username, email, password, false);
            return renderDashboard();
        }
        return renderEdit();
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
        User viewedUser = User.find().byId(userId);
        if (currentUser == null || viewedUser == null) {
            return notFound(error404.render(currentUser, "User not found!"));
        }
        if (currentUser.getId().equals(userId)) {
            return renderDashboard();
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
