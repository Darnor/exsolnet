package repositories;

import models.User;
import org.junit.Before;
import org.junit.Test;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mario on 06.04.16.
 */
public class UserRepositoryTest extends AbstractRepositoryTest {
    private UserRepository userRepository;

    @Before
    public void Setup(){
        userRepository = new UserRepository();
    }

    @Test
    public void checkIfAuthenticateCreatesNewUserWhenNonExisting(){
        String testemail = "nonexistinghans";
        assertTrue(userRepository.findUserWithThisEmail(testemail) == null);
        userRepository.authenticate(testemail, null);
        assertTrue(userRepository.findUserWithThisEmail(testemail) != null);
    }

    @Test
    public void checkIfAuthenticateReturnsUserExisting(){
        String testemail = "Hans";
        assertTrue(userRepository.findUserWithThisEmail(testemail) != null);
        User user = userRepository.authenticate(testemail, null);
        assertEquals(testemail,user.getEmail());
        assertEquals("java.lang.Long", user.getId().getClass().getName());
    }
}
