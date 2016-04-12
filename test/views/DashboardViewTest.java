package views;

import controllers.DashboardController;
import models.Comment;
import models.User;
import models.builders.CommentBuilder;
import org.junit.Test;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static helper.RegexMatcher.matches;
import static models.builders.CommentBuilder.aComment;
import static models.builders.ExerciseBuilder.anExercise;
import static models.builders.SolutionBuilder.aSolution;
import static models.builders.UserBuilder.anUser;
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
        assertThat(html.body(), matches("<a href=.*/exercises.*tags=A.*>A</a>.*0/12"));
        assertThat(html.body(), matches("<a href=.*/exercises.*tags=B.*>B</a>.*7/17"));
    }

    @Test
    public void exerciseCommentsAreRendered() {
        List<Comment> comments = new ArrayList<>();
        User commenter = anUser().withEmail("Hans").build();
        CommentBuilder comment = aComment().withUser(commenter).withExercise(
                anExercise().withTitle("Basic Math").withId(1234L).build()
        );
        comments.add(comment.but().withContent("Comment 1").build());
        comments.add(comment.but().withContent("Comment 2").build());
        comments.add(comment.but().withContent("Comment 3").build());
        comments.add(comment.but().withContent("Comment 4").build());
        comments.add(comment.but().withContent("Comment 5").build());

        Content html = views.html.dashboard.render(null, new ArrayList<>(), comments);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("Comment 1.*Hans.*auf.*Basic Math.*"));
    }

    @Test
    public void solutionCommentsAreRendered() {
        List<Comment> comments = new ArrayList<>();
        User commenter = anUser().withEmail("Hans").build();
        CommentBuilder comment = aComment().withUser(commenter).withSolution(
                aSolution().withExercise(
                        anExercise().withTitle("Basic Math").withId(1234L).build()
                ).build()
        );
        comments.add(comment.but().withContent("Comment 1").build());
        comments.add(comment.but().withContent("Comment 2").build());
        comments.add(comment.but().withContent("Comment 3").build());
        comments.add(comment.but().withContent("Comment 4").build());
        comments.add(comment.but().withContent("Comment 5").build());

        Content html = views.html.dashboard.render(null, new ArrayList<>(), comments);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("Comment 1.*Hans.*auf.*Basic Math.*"));

    }
}
