package controllers;

import helper.AbstractApplicationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.*;

public class UserControllerTest extends AbstractApplicationTest {

    Map<String, String> form;

    @Before
    public void setUp() {
        form = new HashMap<>();
        form.put("username", "Alfred93");
        form.put("email", "alfred.alfredo@hsr.ch");
        form.put("password", "a");
        form.put("password-check", "a");
    }

    @After
    public void cleanUp() {
        form = null;
    }

    @Test
    public void testAuthorizedUserUpdate() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.processUpdate(8000))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testUnauthorizedUserUpdate() {
        form.replace("username", "Antonius96");
        form.replace("email", "antoniues.antoni@hsr.ch");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.processUpdate(8002L))
                            .session("connected", "simon@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }
}
