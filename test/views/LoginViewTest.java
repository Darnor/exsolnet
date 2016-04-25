package views;

import models.builders.UserBuilder;
import org.junit.Test;
import play.twirl.api.Html;
import views.html.login;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class LoginViewTest extends AbstractViewTest{

    @Test
    public void checkIfUserNameGetsRendered(){
        Html html = login.render(UserBuilder.anUser().withUsername("Jhonny").build());
        assertThat(html.body(), containsString("Jhonny"));
    }
}
