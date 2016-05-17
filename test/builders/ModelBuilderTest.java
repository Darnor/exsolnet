package builders;

import helper.AbstractApplicationTest;
import models.*;
import models.builders.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ModelBuilderTest extends AbstractApplicationTest {

    List<Comment> comments;
    List<Exercise> exercises;
    List<Solution> solutions;
    List<Tracking> trackings;
    List<Vote> votes;
    List<Tag> tags;
    User user;
    Exercise exercise;
    Solution solution;
    Tag tag;

    @Before
    public void init() {
        comments = new ArrayList<Comment>();
        comments.add(Comment.findValidById(8000L));
        exercises = new ArrayList<Exercise>();
        exercises.add(Exercise.findById(8004L));
        exercise = Exercise.findById(8004L);
        solutions = new ArrayList<Solution>();
        solutions.add(Solution.findById(8013L));
        solution = Solution.findById(8013L);
        trackings = new ArrayList<Tracking>();
        trackings.add(Tracking.findById(8000L));
        votes = new ArrayList<Vote>();
        votes.add(Vote.findById(8000L));
        tags = new ArrayList<Tag>();
        tags.add(Tag.findById(8000L));
        tag = Tag.findById(8000L);
        user = User.findById(8000L);


    }

    @Test
    public void TestUserBuilder() {


        User user = UserBuilder.anUser().withComments(comments).withEmail("mail@gmail.com").withIsModerator(false).withExercises(exercises).withPassword("password").withSolutions(solutions).withTrackings(trackings).withUsername("anewuser").withVotes(votes).withId(666L).build();
        Assert.assertEquals("666", user.getId().toString());
        Assert.assertEquals("anewuser", user.getUsername());
        Assert.assertEquals("password", user.getPassword());
        Assert.assertEquals(false, user.isModerator());
        Assert.assertEquals(solutions.get(0).getId(), user.getValidSolutions().get(0).getId());
        Assert.assertEquals(trackings.get(0).getId(), user.getTrackings().get(0).getId());
        Assert.assertEquals(votes.get(0).getId(), user.getVotes().get(0).getId());
        Assert.assertEquals(exercises.get(0).getId(), user.getValidExercises().get(0).getId());
    }

    @Test
    public void TestExerciseBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Exercise exercise = ExerciseBuilder.anExercise().withId(666L).withTags(tags)
                .withComments(comments).withContent("content").withSolutions(solutions)
                .withTime(now).withTitle("title").withUser(user).withVotes(votes).build();
        Assert.assertEquals("666", exercise.getId().toString());
        Assert.assertEquals("content", exercise.getContent());
        Assert.assertEquals("title", exercise.getTitle());
        Assert.assertEquals(now, exercise.getTime());
        Assert.assertEquals(null, exercise.getLastChanged());
        Assert.assertEquals(true, exercise.isValid());
        Assert.assertEquals(comments.get(0).getId(), exercise.getComments().get(0).getId());
        Assert.assertEquals(solutions.get(0).getId(), exercise.getSolutions().get(0).getId());
        Assert.assertEquals(tags.get(0).getId(), exercise.getTags().get(0).getId());
        Assert.assertEquals(votes.get(0).getId(), exercise.getVotes().get(0).getId());
        Assert.assertEquals(user.getId(), exercise.getUser().getId());

    }

    @Test
    public void TestSolutionBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Solution solution = SolutionBuilder.aSolution().withIsOfficial(true)
                .withContent("content").withUser(user).withComments(comments)
                .withExercise(exercise).withId(666L).withTime(now).withVotes(votes)
                .build();

        Assert.assertEquals("666", solution.getId().toString());
        Assert.assertEquals("content", solution.getContent());
        Assert.assertEquals(now, solution.getTime());
        Assert.assertEquals(null, solution.getLastChanged());
        Assert.assertEquals(true, solution.isOfficial());
        Assert.assertEquals(true, solution.isOfficial());
        Assert.assertEquals(0, solution.getPoints());
        Assert.assertEquals(comments.get(0).getId(), solution.getComments().get(0).getId());
        Assert.assertEquals(votes.get(0).getId(), solution.getVotes().get(0).getId());
        Assert.assertEquals(exercise.getId(), solution.getExercise().getId());
        Assert.assertEquals(user.getId(), solution.getUser().getId());
    }

    @Test
    public void TestTagBuilder() {
        Tag tag = TagBuilder.aTag().withIsMainTag(true).withName("name").withId(666L)
                .withExercises(exercises).withLongName("longname").withTrackings(trackings)
                .build();
        Assert.assertEquals("666", tag.getId().toString());
        Assert.assertEquals("name", tag.getName());
        Assert.assertEquals("longname", tag.getLongName());
        Assert.assertEquals(true, tag.isMainTag());
        Assert.assertEquals(trackings.get(0).getId(), tag.getTrackings().get(0).getId());
        Assert.assertEquals(exercises.get(0).getId(), tag.getExercises().get(0).getId());
    }

    @Test
    public void TestVoteBuilder() {
        Vote vote = VoteBuilder.aVote().withExercise(exercise).withId(666L).withSolution(solution)
                .withUser(user).withValue(1).build();
        Assert.assertEquals("666", vote.getId().toString());
        Assert.assertEquals(exercise.getId(), vote.getExercise().getId());
        Assert.assertEquals(solution.getId(), vote.getSolution().getId());
        Assert.assertEquals(user.getId(), vote.getUser().getId());
        Assert.assertEquals(1, vote.getValue());

    }

    @Test
    public void TestTrackingBuilder() {
        Tracking tracking = TrackingBuilder.aTracking().withId(666L).withTag(tag).withUser(user).build();
        Assert.assertEquals("666", tracking.getId().toString());
        Assert.assertEquals(tag.getId(), tracking.getTag().getId());
        Assert.assertEquals(user.getId(), tracking.getUser().getId());
    }

}
