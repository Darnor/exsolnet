package models;

import helper.AbstractApplicationTest;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class UserTest extends AbstractApplicationTest {

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithEmail(){
        String testemail = "hans@hsr.ch";
        User existingUser = User.findUserByEmail(testemail);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testemail, "");

        assertEquals(testemail,user.getEmail());
        assertEquals(userId, user.getId());
    }

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithUsername(){
        String testusername = "Hans";
        User existingUser = User.findUserByUsername(testusername);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testusername, "");

        assertEquals(testusername,user.getUsername());
        assertEquals(userId, user.getId());
    }

    @Test
    public void testHasSolved(){
        String testusername = "Franz";
        User user = User.findUserByUsername(testusername);
        assertTrue(user.hasSolved(8000));
    }

    @Test
    public void testHasNotSolved(){
        String testusername = "Franz";
        User user = User.findUserByUsername(testusername);
        assertFalse(user.hasSolved(8007));
    }

    @Test
    public void testUserPoints(){
        String testUserFranz = "Franz";
        String testUserHans = "Hans";
        String testUserBlubberduck = "Blubberduck";
        User userFranz = User.findUserByUsername(testUserFranz);
        User userHans = User.findUserByUsername(testUserHans);
        User userBlubberduck = User.findUserByUsername(testUserBlubberduck);
        assertEquals(-1,userFranz.getPoints());
        assertEquals(-13,userHans.getPoints());
        assertEquals(8,userBlubberduck.getPoints());
    }
}
