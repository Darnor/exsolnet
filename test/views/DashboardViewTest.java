package views;

import models.*;
import models.builders.*;
import org.junit.Test;
import play.twirl.api.Content;
import views.html.userDashboard;
import views.html.userViews.followedTags;
import views.html.userViews.recentComments;
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
                followedTags.render(user, user),
                recentComments.render(user, user),
                userExerciseList.render(user, user),
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

        List<Exercise> exercisesA = new ArrayList<>();
        exercisesA.add(exerciseA);
        exercisesA.add(exerciseB);

        List<Exercise> exercisesB = new ArrayList<>();
        exercisesB.add(exerciseC);
        exercisesB.add(exerciseD);
        exercisesB.add(exerciseE);

        aTag.setExercises(exercisesA);
        bTag.setExercises(exercisesB);

        List<Solution> solutions = new ArrayList<>();
        solutions.add(SolutionBuilder.aSolution().withId(1L).withExercise(exercisesB.get(0)).build());
        solutions.add(SolutionBuilder.aSolution().withId(2L).withExercise(exercisesB.get(2)).build());

        User user = anUser().withId(1L).withSolutions(solutions).build();

        Tracking t1 = TrackingBuilder.aTracking().withId(1L).withUser(user).withTag(aTag).build();
        Tracking t2 = TrackingBuilder.aTracking().withId(2L).withUser(user).withTag(bTag).build();

        user.setTrackings(Arrays.asList(t1, t2));

        Content html = userDashboard.render(
                user,
                followedTags.render(user, user),
                recentComments.render(user, user),
                userExerciseList.render(user, user),
                userSolutionList.render(new ArrayList<>())
        );
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("<a href=.*/exercises.*tags=A.*>.*A.*</a>.*0/2"));
        assertThat(html.body(), matches("<a href=.*/exercises.*tags=B.*>.*B.*</a>.*2/3"));
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

        Exercise e1 = ExerciseBuilder.anExercise().withId(1L).withUser(commenter).withComments(comments).build();

        commenter.setExercises(Arrays.asList(e1));

        Content html = userDashboard.render(
                commenter,
                followedTags.render(commenter, commenter),
                recentComments.render(commenter, commenter),
                userExerciseList.render(commenter, commenter),
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

        Solution s1 = SolutionBuilder.aSolution().withId(1L).withUser(commenter).withComments(comments).build();

        commenter.setSolutions(Arrays.asList(s1));

        Content html = userDashboard.render(
                commenter,
                followedTags.render(commenter, commenter),
                recentComments.render(commenter, commenter),
                userExerciseList.render(commenter, commenter),
                userSolutionList.render(new ArrayList<>())
        );
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), matches("Comment 1.*Hans.*auf.*Basic Math.*"));
    }
}
