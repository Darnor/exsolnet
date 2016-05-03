package integration;

import models.Exercise;
import models.Solution;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class VotingIT extends AbstractIntegrationTest {

    @Test
    public void testExerciseDownAndFollowingUpvote() {
        long exerciseId = 8011;
        as(FRANZ, browser -> {
            long oldpoints = Exercise.find().where().eq("id", exerciseId).findUnique().getPoints();
            browser.goTo("/exercises/8011");
            browser.click("#downVoteExercise");

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#exercisePoints").containsText("" + (oldpoints - 1));

            assertThat(Exercise.findById(exerciseId).getPoints(), is(oldpoints - 1));

            oldpoints = Exercise.find().where().eq("id", exerciseId).findUnique().getPoints();
            browser.click("#upVoteExercise");

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#exercisePoints").containsText("" + (oldpoints + 2));

            assertThat(Exercise.findById(exerciseId).getPoints(), is(oldpoints + 2));
        });
    }

    @Test
    public void testSolutionUpvoteAndFollowingDownVote() {
        long solutionId = 8003;
        as(FRANZ, browser -> {
            long oldpoints = Solution.find().where().eq("id", solutionId).findUnique().getPoints();
            browser.goTo("/exercises/8001");
            browser.click("#upVoteSolution" + solutionId);

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#solutionPoints" + solutionId).containsText("" + (oldpoints + 1));

            assertThat(Solution.find().where().eq("id", solutionId).findUnique().getPoints(), is(oldpoints + 1));

            oldpoints = Solution.find().where().eq("id", solutionId).findUnique().getPoints();
            browser.click("#downVoteSolution" + solutionId);

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#solutionPoints" + solutionId).containsText("" + (oldpoints - 2));

            assertThat(Solution.find().where().eq("id", solutionId).findUnique().getPoints(), is(oldpoints - 2));
        });
    }

    @Test
    public void testExerciseDoubleDownVote() {
        long exerciseId = 8011;
        as(FRANZ, browser -> {
            long oldpoints = Exercise.find().where().eq("id", exerciseId).findUnique().getPoints();
            browser.goTo("/exercises/8011");
            browser.click("#downVoteExercise");

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#exercisePoints").containsText("" + (oldpoints - 1));

            assertThat(Exercise.findById(exerciseId).getPoints(), is(oldpoints - 1));

            oldpoints = Exercise.find().where().eq("id", exerciseId).findUnique().getPoints();
            browser.click("#downVoteExercise");

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#exercisePoints").containsText("" + (oldpoints + 1));

            assertThat(Exercise.findById(exerciseId).getPoints(), is(oldpoints + 1));
        });
    }

    @Test
    public void testSolutionDoubleUpVote() {
        long solutionId = 8002;
        as(FRANZ, browser -> {
            long oldpoints = Solution.find().where().eq("id", solutionId).findUnique().getPoints();
            browser.goTo("/exercises/8001");
            browser.click("#upVoteSolution" + solutionId);

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#solutionPoints" + solutionId).containsText("" + (oldpoints + 1));

            assertThat(Solution.find().where().eq("id", solutionId).findUnique().getPoints(), is(oldpoints + 1));

            oldpoints = Solution.find().where().eq("id", solutionId).findUnique().getPoints();
            browser.click("#upVoteSolution" + solutionId);

            //wait for ajax
            browser.await().atMost(2, TimeUnit.SECONDS).until("#solutionPoints" + solutionId).containsText("" + (oldpoints - 1));

            assertThat(Solution.find().where().eq("id", solutionId).findUnique().getPoints(), is(oldpoints - 1));
        });
    }

}
