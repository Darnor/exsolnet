package controllers;

import com.google.inject.Inject;
import models.Comment;
import models.Tag;
import play.mvc.Result;
import repositories.CommentRepository;
import repositories.TagRepository;
import services.SessionService;
import views.html.dashboard;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Results.ok;

/**
 * This controller contains an action to handle HTTP requests
 * to the users dashboard
 */
public class DashboardController {
    @Inject
    SessionService sessionService;

    @Inject
    TagRepository tagRepo;

    @Inject
    CommentRepository commentRepo;

    public Result renderDashboard(){
        return ok(dashboard.render(sessionService.getUsername(), getSubscribedTags(), getRecentComments()));
    }

    private List<Comment> getRecentComments() {
        return commentRepo.getRecentComments(sessionService.getCurrentUser());
    }

    private List<Tag> getSubscribedTags() {
        return tagRepo.getTrackedTags(sessionService.getCurrentUser());
    }
}
