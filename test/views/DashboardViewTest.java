package views;

import models.*;
import models.builders.*;
import org.junit.Test;
import play.twirl.api.Content;
import views.html.userDashboard;
import views.html.userViews.recentComments;
import views.html.userViews.followedTags;
import views.html.userViews.userExerciseList;
import views.html.userViews.userSolutionList;

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

public class DashboardViewTest extends AbstractViewTest{

    @Test
    public void usernameIsRendered() {
        User user = UserBuilder.anUser().withId(1L).withUsername("Franz").build();
        Content html = userDashboard.render(
                user,
                followedTags.render(user, new ArrayList<>()),
                recentComments.render(new ArrayList<>()),
                userExerciseList.render(new ArrayList<>()),
                userSolutionList.render(new ArrayList<>())
        );
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Franz"));
    }

    @Test
    public void tagsAreRendered() {
        Tag aTag = TagBuilder.aTag().withName("A").withId(1L).build();
        Tag bTag = TagBuilder.aTag().withName("B").withId(2L).build();

        Exercise exerciseA = ExerciseBuilder.anExercise().withId(1L).withTags(Arrays.asList(aTag)).build();
        Exercise exerciseB = ExerciseBuilder.anExercise().withId(2L).withTags(Arrays.asList(aTag)).build();

        Exercise exerciseC = ExerciseBuilder.anExercise().withId(3L).withTags(Arrays.asList(bTag)).build();
        Exercise exerciseD = ExerciseBuilder.anExercise().withId(4L).withTags(Arrays.asList(bTag)).build();
        Exercise exerciseE = ExerciseBuilder.anExercise().withId(5L).withTags(Arrays.asList(bTag)).build();

        List<Exercise> exercisesA = Arrays.asList(exerciseA, exerciseB);
        List<Exercise> exercisesB = Arrays.asList(exerciseC, exerciseD, exerciseE);

        aTag.setExercises(exercisesA);
        bTag.setExercises(exercisesB);

        List<Solution> solutions = Arrays.asList(
                SolutionBuilder.aSolution().withId(1L).withExercise(exercisesB.get(0)).build(),
                SolutionBuilder.aSolution().withId(2L).withExercise(exercisesB.get(2)).build()
        );

        User user = anUser().withId(1L).withSolutions(solutions).build();

        Content html = userDashboard.render(
                user,
                followedTags.render(user, Arrays.asList(aTag, bTag)),
                recentComments.render(new ArrayList<>()),
                userExerciseList.render(new ArrayList<>()),
                userSolutionList.render(new ArrayList<>())
        );
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("<a href=.*/exercises.*tags=A.*>A</a>.*0/2"));
        assertThat(html.body(), matches("<a href=.*/exercises.*tags=B.*>B</a>.*2/3"));
    }

    @Test
    public void exerciseCommentsAreRendered() {
        List<Comment> comments = new ArrayList<>();
        User commenter = anUser().withId(1L).withUsername("Hans").build();
        CommentBuilder comment = aComment().withUser(commenter).withExercise(
                anExercise().withTitle("Basic Math").withId(1234L).build()
        );
        comments.add(comment.but().withContent("Comment 1").build());
        comments.add(comment.but().withContent("Comment 2").build());
        comments.add(comment.but().withContent("Comment 3").build());
        comments.add(comment.but().withContent("Comment 4").build());
        comments.add(comment.but().withContent("Comment 5").build());

        Content html = userDashboard.render(
                commenter,
                followedTags.render(commenter, new ArrayList<>()),
                recentComments.render(comments),
                userExerciseList.render(new ArrayList<>()),
                userSolutionList.render(new ArrayList<>())
        );
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("Comment 1.*Hans.*auf.*Basic Math.*"));
    }

    @Test
    public void solutionCommentsAreRendered() {
        List<Comment> comments = new ArrayList<>();
        User commenter = anUser().withId(1L).withUsername("Hans").build();
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

        Content html = userDashboard.render(
                commenter,
                followedTags.render(commenter, new ArrayList<>()),
                recentComments.render(comments),
                userExerciseList.render(new ArrayList<>()),
                userSolutionList.render(new ArrayList<>())
        );
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("Comment 1.*Hans.*auf.*Basic Math.*"));
    }
}
