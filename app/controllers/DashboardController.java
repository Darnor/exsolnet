package controllers;

import com.google.inject.Inject;
import models.Comment;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.CommentRepository;
import repositories.TagRepository;
import services.SessionService;
import views.html.dashboard;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the users dashboard
 */
public class DashboardController extends Controller{
    @Inject
    SessionService sessionService;

    @Inject
    TagRepository tagRepo;


    @Inject
    CommentRepository commentRepo;


    //setters used for mocking
    public void setCommentRepo(CommentRepository commentRepo) {
        this.commentRepo = commentRepo;
    }

    public void setTagRepo(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Result renderDashboard(){
        if(!sessionService.isLoggedin()){
            return LoginController.redirectIfNotLoggedIn();
        }
        return ok(dashboard.render(sessionService.getUsername(), getSubscribedTags(), getRecentComments()));
    }

    private List<Comment> getRecentComments() {
        return commentRepo.getRecentComments(sessionService.getCurrentUser());
    }

    private List<TagEntry> getSubscribedTags() {
        return tagRepo.getTrackedTags(sessionService.getCurrentUser()).stream().map(tag ->
            new TagEntry(
                    tag.getName(),
                    tag.getExercises().size(),
                    tagRepo.getNofCompletedExercises(tag, sessionService.getCurrentUser())
            )
        ).collect(Collectors.toList());
    }

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
