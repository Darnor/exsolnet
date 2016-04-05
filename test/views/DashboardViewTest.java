package views;

import controllers.DashboardController;
import models.Comment;
import models.User;
import org.junit.Test;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static models.Comment.CommentBuilder.aComment;
import static models.User.UserBuilder.anUser;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by tourn on 4.4.16.
 */
public class DashboardViewTest {

    @Test
    public void usernameIsRendered() {
        Content html = views.html.dashboard.render("Franz", new ArrayList<>(), new ArrayList<>());
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Franz"));
    }

    @Test
    public void tagsAreRendered() {
        List<DashboardController.TagEntry> tags = Arrays.asList(
                new DashboardController.TagEntry("A", 0, 12),
                new DashboardController.TagEntry("B", 7, 17)
        );
        Content html = views.html.dashboard.render(null, tags, new ArrayList<>());
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<li>A (0/12)</li>"));
        assertThat(html.body(), containsString("<li>B (7/17)</li>"));
    }

    @Test
    public void commentsAreRendered() {
        List<Comment> comments = new ArrayList<>();
        User commenter = anUser().withEmail("Hans").build();
        Comment.CommentBuilder comment = aComment().withUser(commenter);
        comments.add(comment.but().withContent("Comment 1").build());
        comments.add(comment.but().withContent("Comment 2").build());
        comments.add(comment.but().withContent("Comment 3").build());
        comments.add(comment.but().withContent("Comment 4").build());
        comments.add(comment.but().withContent("Comment 5").build());

        Content html = views.html.dashboard.render(null, new ArrayList<>(), comments);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<li>Comment 1</li>"));
        assertThat(html.body(), containsString("<li>Comment 5</li>"));

    }
}
