package controllers;

import models.Comment;
import models.Exercise;
import models.Tag;
import models.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Result;
import repositories.CommentRepository;
import repositories.TagRepository;
import services.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static models.Comment.CommentBuilder.aComment;
import static models.Exercise.ExerciseBuilder.anExercise;
import static models.Tag.TagBuilder.aTag;
import static models.User.UserBuilder.anUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.contentAsString;

/**
 * Created by tourn on 4.4.16.
 */
@Ignore //FIXME
public class DashboardControllerTest {

    DashboardController controller;
    TagRepository tagRepo;
    CommentRepository commentRepo;
    SessionService sessionService;

    @Before
    public void setUp(){
        controller = new DashboardController();
        tagRepo = mock(TagRepository.class);
        commentRepo = mock(CommentRepository.class);
        sessionService = mock(SessionService.class);
        controller.setSessionService(sessionService);
        controller.setCommentRepo(commentRepo);
        controller.setTagRepo(tagRepo);
    }

    @Test
    public void testUsernameIsRendered(){
        when(sessionService.getCurrentUser()).thenReturn(anUser().withEmail("Franz").build());
        Result result = controller.renderDashboard();
        assertThat(contentAsString(result), containsString("Franz"));
    }

    @Test
    public void testCommentsAreShown(){
        List<Comment> comments = new ArrayList<>();
        User commenter = anUser().withEmail("Hans").build();
        Comment.CommentBuilder comment = aComment().withUser(commenter);
        comments.add(comment.but().withContent("Comment 1").build());
        comments.add(comment.but().withContent("Comment 2").build());
        comments.add(comment.but().withContent("Comment 3").build());
        comments.add(comment.but().withContent("Comment 4").build());
        comments.add(comment.but().withContent("Comment 5").build());
        when(commentRepo.getRecentComments(any())).thenReturn(comments);

        Result result = controller.renderDashboard();
        assertThat(contentAsString(result), containsString("Comment 1"));
        assertThat(contentAsString(result), containsString("Comment 5"));
    }

    @Test
    public void testTrackedTagsAreShown(){
        List<Tag> tags = new ArrayList<>();
        User subscriber = anUser().withEmail("Hans").build();
        Exercise e1 = anExercise().withTitle("Exercise 1").build();
        Exercise e2 = anExercise().withTitle("Exercise 2").build();
        Exercise e3 = anExercise().withTitle("Exercise 3").build();
        tags.add(aTag().withName("A").withExercises(Arrays.asList(e1, e2)).build());
        tags.add(aTag().withName("B").withExercises(Arrays.asList(e1, e2, e3)).build());
        when(tagRepo.getTrackedTags(any())).thenReturn(tags);

        Result result = controller.renderDashboard();
        assertThat(contentAsString(result), containsString("A (0/2)"));
        assertThat(contentAsString(result), containsString("B (0/3)"));
    }


}
