package controllers;

import helper.AbstractApplicationTest;
import models.User;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class UserControllerTest extends AbstractApplicationTest {

    @Test
    public void testAuthorizedUserUpdate() {
        Map<String, String> form = new HashMap<>();
        form.put("username", "Alfred93");
        form.put("email", "alfred.alfredo@hsr.ch");
        form.put("password", "a");
        form.put("password-check", "a");

        running(fakeApplication(), () -> {
            assertNotNull(User.findByEmail("hans@hsr.ch"));
            assertNull(User.findByEmail(form.get("email")));
            assertNull(User.findByUsername(form.get("username")));
            Result result = route(
                    fakeRequest(routes.UserController.processUpdate(8001))
                            .session("connected", "hans@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            User user = User.findByUsername(form.get("username"));
            assertNotNull(User.findByEmail("hans@hsr.ch"));
            assertNull(User.findByUsername("hans"));
            assertNotNull(user);
            assertThat(user.getUsername(), is(form.get("username")));
        });
    }

    @Test
    public void testAuthorizedUserUpdateNonModeratirIsStillNonModerator() {
        Map<String, String> form = new HashMap<>();
        form.put("username", "Jusuf");
        form.put("email", "jus.uf@hsr.ch");
        form.put("password", "a");
        form.put("password-check", "a");

        running(fakeApplication(), () -> {
            assertNotNull(User.findByEmail("tony@hsr.ch"));
            assertNull(User.findByEmail(form.get("email")));
            assertNull(User.findByUsername(form.get("username")));
            Result result = route(
                    fakeRequest(routes.UserController.processUpdate(8008))
                            .session("connected", "tony@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            User user = User.findByUsername(form.get("username"));
            assertNotNull(User.findByEmail("tony@hsr.ch"));
            assertNull(User.findByUsername("tony"));
            assertNotNull(user);
            assertThat(user.getUsername(), is(form.get("username")));
            assertThat(user.isModerator(), is(false));
        });
    }

    private void testFailedUserUpdate(Map<String, String> form) {
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
            assertNotNull(User.findByEmail("franz@hsr.ch"));
        });
    }

    @Test
    public void testAuthorizedFailedUserUpdateEmptyUsername() {
        Map<String, String> form = new HashMap<>();
        form.put("username", "");
        form.put("email", "alfred.alfredo@hsr.ch");
        form.put("password", "a");
        form.put("password-check", "a");

        testFailedUserUpdate(form);
    }

    @Test
    public void testAuthorizedFailedUserUpdateWrongUsernameFormat() {
        Map<String, String> form = new HashMap<>();
        form.put("username", "<script>alert('hallo')</script>");
        form.put("email", "alfred.alfredo@hsr.ch");
        form.put("password", "a");
        form.put("password-check", "a");

        testFailedUserUpdate(form);
    }

    @Test
    public void testAuthorizedUserUpdateOnlyUsername() {
        Map<String, String> form = new HashMap<>();
        form.put("username", "Norbi94");
        form.put("email", "nerbert.norberto@hsr.ch");
        form.put("password", "");
        form.put("password-check", "");

        running(fakeApplication(), () -> {
            User originalUser = User.findByEmail("peter@hsr.ch");
            assertNotNull(originalUser);
            assertNull(User.findByEmail(form.get("email")));
            assertNull(User.findByUsername(form.get("username")));
            String originalPassword = originalUser.getPassword();
            Result result = route(
                    fakeRequest(routes.UserController.processUpdate(7999))
                            .session("connected", "peter@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            User user = User.findByUsername(form.get("username"));
            assertNotNull(User.findByEmail("peter@hsr.ch"));
            assertNull(User.findByUsername("peter"));
            assertNotNull(user);
            assertThat(user.getUsername(), is(form.get("username")));
            assertThat(user.getPassword(), is(originalPassword));
            assertThat(user.isModerator(), is(true));
        });
    }

    @Test
    public void testNotAuthorizedUserUpdate() {
        Map<String, String> form = new HashMap<>();
        form.put("username", "Antonius96");
        form.put("email", "antonius.antoni@hsr.ch");
        form.put("password", "a");
        form.put("password-check", "a");

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
    public void testRenderExternalUser() {
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
    public void testRenderNotExistingUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderUser(-1))
                            .session("connected", "simon@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testRenderUserUnauthorized() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderUser(8000L))
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/login"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testRenderEditUnauthorized() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderEdit())
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/login"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testRenderEditAuthorized() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.UserController.renderEdit())
                            .session("connected", "simon@hsr.ch")
            );
            assertThat(result.status(), is(OK));
        });
    }
}
