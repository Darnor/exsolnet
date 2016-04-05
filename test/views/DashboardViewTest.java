package views;

import models.Comment;
import models.Exercise;
import models.Tag;
import models.User;
import org.junit.Test;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static models.Comment.CommentBuilder.aComment;
import static models.Exercise.ExerciseBuilder.anExercise;
import static models.Tag.TagBuilder.aTag;
import static models.User.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
        List<Tag> tags = new ArrayList<>();
        //User subscriber = anUser().withEmail("Hans").build();
        Exercise e1 = anExercise().withTitle("Exercise 1").build();
        Exercise e2 = anExercise().withTitle("Exercise 2").build();
        Exercise e3 = anExercise().withTitle("Exercise 3").build();
        tags.add(aTag().withName("A").withExercises(Arrays.asList(e1, e2)).build());
        tags.add(aTag().withName("B").withExercises(Arrays.asList(e1, e2, e3)).build());

        Content html = views.html.dashboard.render(null, tags, new ArrayList<>());
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<li>A (0/2)</li>"));
        assertThat(html.body(), containsString("<li>B (0/3)</li>"));
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
