package models;

import helper.AbstractApplicationTest;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class UserTest extends AbstractApplicationTest {

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithEmail(){
        String testemail = "hans@hsr.ch";
        User existingUser = User.findByEmail(testemail);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testemail, "");

        assertEquals(testemail,user.getEmail());
        assertEquals(userId, user.getId());
    }

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithUsername(){
        String testusername = "Hans";
        User existingUser = User.findByUsername(testusername);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testusername, "");

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
}
