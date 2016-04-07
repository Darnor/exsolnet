package models;

import org.junit.Test;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by mario on 06.04.16.
 */
public class UserTest extends AbstractModelTest {

    @Test
    public void checkIfAuthenticateCreatesNewUserWhenNonExisting(){
        String testemail = "nonexistinghans";
        assertNull(User.findUser(testemail));
        User.authenticate(testemail, null);
        assertNotNull(User.findUser(testemail));
    }

    @Test
    public void checkIfAuthenticateReturnsUserExisting(){
        String testemail = "Hans";
        assertNotNull(User.findUser(testemail));
        User user = User.authenticate(testemail, null);
        assertEquals(testemail,user.getEmail());
        assertNotNull(user.getId());
    }
}
