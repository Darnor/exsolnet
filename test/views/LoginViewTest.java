package views;

import org.junit.Test;

/**
 * Created by mario on 05.04.16.
 */
public class LoginViewTest {

    @Test
    public void checkIfUserNameGetsRendered(){
        views.html.login.render("Jhonny");

    }

}
