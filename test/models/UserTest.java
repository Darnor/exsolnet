package models;

import helper.AbstractApplicationTest;
import models.builders.ExerciseBuilder;
import models.builders.SolutionBuilder;
import models.builders.TagBuilder;
import models.builders.UserBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class UserTest extends AbstractApplicationTest {

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithEmail(){
        String testemail = "hans@hsr.ch";
        User existingUser = User.findByEmail(testemail);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testemail, "a");

        assertEquals(testemail,user.getEmail());
        assertEquals(userId, user.getId());
    }

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithUsername(){
        String testusername = "Hans";
        User existingUser = User.findByUsername(testusername);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testusername, "a");

        assertEquals(testusername,user.getUsername());
        assertEquals(userId, user.getId());
    }

    @Test
    public void testHasSolved(){
        String testusername = "Franz";
        User user = User.findByUsername(testusername);
        assertTrue(user.hasSolved(8000));
    }

    @Test
    public void testHasNotSolved(){
        String testusername = "Franz";
        User user = User.findByUsername(testusername);
        assertFalse(user.hasSolved(8007));
    }

    @Test
    public void testUserPoints(){
        String testUserFranz = "Franz";
        String testUserHans = "Hans";
        String testUserBlubberduck = "Blubberduck";
        User userFranz = User.findByUsername(testUserFranz);
        User userHans = User.findByUsername(testUserHans);
        User userBlubberduck = User.findByUsername(testUserBlubberduck);
        assertEquals(-1,userFranz.getPoints());
        assertEquals(-13,userHans.getPoints());
        assertEquals(8,userBlubberduck.getPoints());
    }

    @Test
    public void testIsSolved(){
        User franz = User.findByUsername("Franz");
        Exercise ex = Exercise.findById(8001L);
        assertTrue("Franz has solved Exercise 8001", ex.isSolvedBy(franz));
    }

    @Test
    public void testIsNotSolved(){
        User franz = User.findByUsername("Franz");
        Exercise ex = Exercise.findById(8007L);
        assertFalse("Franz has not solved Exercise 8007", ex.isSolvedBy(franz));
    }

    @Test
    public void testGetNoOfCompletedExercisesValidOnly() {
        Tag t1 = TagBuilder.aTag().withId(1L).build();
        Tag t2 = TagBuilder.aTag().withId(2L).build();
        Exercise e1 = ExerciseBuilder.anExercise().withId(1L).withIsValid(false).withTags(Arrays.asList(t1)).build();
        Exercise e2 = ExerciseBuilder.anExercise().withId(2L).withIsValid(true).withTags(Arrays.asList(t1)).build();
        Exercise e3 = ExerciseBuilder.anExercise().withId(3L).withIsValid(true).withTags(Arrays.asList(t2)).build();

        Solution s1 = SolutionBuilder.aSolution().withId(1L).withExercise(e3).withIsValid(false).build();
        Solution s2 = SolutionBuilder.aSolution().withId(2L).withExercise(e2).withIsValid(true).build();
        Solution s3 = SolutionBuilder.aSolution().withId(3L).withExercise(e1).but().build();

        List<Exercise> exercises = new ArrayList<>();
        exercises.add(e1);
        exercises.add(e2);
        exercises.add(e3);

        List<Solution> solutions = new ArrayList<>();
        solutions.add(s1);
        solutions.add(s2);
        solutions.add(s3);

        User user = UserBuilder.anUser().withExercises(exercises).withSolutions(solutions).withId(1L).build();

        assertEquals(2, user.getValidExercises().size());
        assertEquals(2, user.getValidSolutions().size());
        assertEquals(1, user.getNofCompletedExercisesByTag(t1));
    }
}
