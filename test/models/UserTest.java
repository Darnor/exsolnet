package models;

import helper.AbstractApplicationTest;
import org.junit.Test;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class UserTest extends AbstractApplicationTest {

    @Test
    public void checkIfAuthenticateCreatesNewUserWhenNonExisting(){
        String testemail = "nonexistinghans@hsr.ch";
        assertNull(User.findUserByEmail(testemail));
        User.authenticate(testemail, null);
        assertNotNull(User.findUserByEmail(testemail));
    }

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithEmail(){
        String testemail = "hans@hsr.ch";
        User existingUser = User.findUserByEmail(testemail);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testemail, null);

        assertEquals(testemail,user.getEmail());
        assertEquals(userId, user.getId());
    }

    @Test
    public void checkIfAuthenticateReturnsUserExistingWithUsername(){
        String testusername = "Hans";
        User existingUser = User.findUserByUsername(testusername);
        assertNotNull(existingUser);
        Long userId = existingUser.getId();

        User user = User.authenticate(testusername, null);

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
}
