package models;

import org.junit.Test;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class UserTest extends AbstractModelTest {

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
}
