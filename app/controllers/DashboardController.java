package controllers;

import models.Comment;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.dashboard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for DashboardView
 */
@Security.Authenticated(Secured.class)
public class DashboardController extends Controller{

    /**
     * Render the dashboard route
     * @return the result
     */
    public Result renderDashboard(){
        return ok(dashboard.render(SessionService.getCurrentUserEmail(), getSubscribedTags(), getRecentComments()));
    }

    /**
     * @return a list of recent comments on posts made by the user
     */
    private List<Comment> getRecentComments() {
        return Comment.getRecentComments(SessionService.getCurrentUser());
    }

    /**
     * @return a list of tags subscribed by the user, including information on how many exercises they answered
     */
    private List<TagEntry> getSubscribedTags() {
        return SessionService.getCurrentUser().getTrackedTags().stream().map(tag ->
            new TagEntry(
                    tag.getName(),
                    tag.getNofCompletedExercises(SessionService.getCurrentUser()),
                    tag.getExercises().size()
            )
        ).collect(Collectors.toList());
    }

    /**
     * A entry for the tag enumeration, used by the dashboard model
     */
    public static class TagEntry{
        public final String name;
        public final long progress;
        public final long total;

        public TagEntry(String name, long progress, long total) {
            this.name = name;
            this.progress = progress;
            this.total = total;
        }
    }
}
