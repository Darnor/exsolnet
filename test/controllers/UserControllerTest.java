package controllers;

import helper.AbstractApplicationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
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
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testAuthorizedFailedUserUpdate() {
        form.replace("username", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.processUpdate(8000))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/user/edit"));
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

    @Test
    public void testRenderAuthoriedExternalUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderUser(8000L))
                            .session("connected", "franz@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testRenderUserDashboard() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderUser(8000L))
                            .session("connected", "simon@hsr.ch")
            );
            assertThat(result.status(), is(OK));
        });
    }

    @Test
    public void testRenderUnauthorized() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderUser(-1))
                            .session("connected", "simon@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }
}
