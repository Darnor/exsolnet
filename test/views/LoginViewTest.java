package views;

import org.junit.Test;
import play.twirl.api.Html;
import views.html.login;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by mario on 05.04.16.
 */
public class LoginViewTest {

    @Test
    public void checkIfUserNameGetsRendered(){
        Html html = login.render("Jhonny");
        assertThat(html.body(), containsString("Jhonny"));
    }
}