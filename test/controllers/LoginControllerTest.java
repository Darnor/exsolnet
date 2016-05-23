package controllers;

import helper.AbstractApplicationTest;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class LoginControllerTest extends AbstractApplicationTest {

    Map<String, String> form;

    @Before
    public void setUp() {
        form = new HashMap<>();
        form.put("username", "testUser99");
        form.put("email", "testUser@test.test");
        form.put("password", "a");
        form.put("password-check", "a");
    }

    @After
    public void cleanUp() {
        form = null;
    }

    @Test
    public void testRegisterNewUser() {
        Map<String, String> session = new HashMap<>();
        session.put("connected", "testUser@test.test");

        running(fakeApplication(), () -> {
            assertNull(User.findByEmail("testUser@test.test"));
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(session));
            User user = User.findByEmail("testUser@test.test");
            assertNotNull(user);
            assertThat(user.getUsername(), is("testUser99"));
        });
    }

    @Test
    public void testRegisterExistingUser() {
        form.replace("username", "tony");
        form.replace("email", "testUser100@test.test");

        running(fakeApplication(), () -> {
            assertNotNull(User.findByEmail("tony@hsr.ch"));
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=tony&email=testUser100%40test.test"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
            User user = User.findByEmail("testUser100@test.test");
            assertNull(user);
        });
    }

    @Test
    public void testRegisterFailedEmptyUserName() {
        form.replace("username", "");
        form.replace("email", "testUser2@test.test");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?email=testUser2%40test.test"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
            assertNull(User.findByEmail("testUser2@test.test"));
        });
    }

    @Test
    public void testRegisterFailedEmptyEmail() {
        form.replace("username", "testUser98");
        form.replace("email", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=testUser98"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testRegisterFailedWrongEmailFormat() {
        form.replace("username", "testUser97");
        form.replace("email", "a@a");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=testUser97&email=a%40a"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    private void testRegisterRedirectLocationAndSession(String resultLocation, Map<String, String> resultSession) {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is(resultLocation));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(resultSession));
        });
    }

    @Test
    public void testRegisterFailedEmptyPassword() {
        form.replace("username", "testUser96");
        form.replace("email", "testUser3@test.test");
        form.replace("password", "");
        form.replace("password-check", "");

        testRegisterRedirectLocationAndSession("/register?username=testUser96&email=testUser3%40test.test", new HashMap<>());
    }

    @Test
    public void testRegisterFailedNotTheSamePassword() {
        form.replace("username", "testUser96");
        form.replace("email", "testUser3@test.test");
        form.replace("password", "a");
        form.replace("password-check", "ab");

        testRegisterRedirectLocationAndSession("/register?username=testUser96&email=testUser3%40test.test", new HashMap<>());
    }

    private void testLoginRedirectLocationAndSession(String resultLocation, Map<String, String> resultSession) {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processLogin())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is(resultLocation));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(resultSession));
        });
    }

    @Test
    public void testUserLoginValidEmail() {
        form.clear();
        form.put("emailorusername", "franz@hsr.ch");
        form.put("password", "a");

        Map<String, String> session = new HashMap<>();
        session.put("connected", "franz@hsr.ch");

        testLoginRedirectLocationAndSession("/", session);
    }

    @Test
    public void testUserLoginValidUserName() {
        form.clear();
        form.put("emailorusername", "franz");
        form.put("password", "a");

        Map<String, String> session = new HashMap<>();
        session.put("connected", "franz@hsr.ch");

        testLoginRedirectLocationAndSession("/", session);
    }

    @Test
    public void testUserLoginInvalidData() {
        form.clear();
        form.put("emailorusername", "hax0r-1337");
        form.put("password", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processLogin())
                            .bodyForm(form)
            );

            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/login"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testRenderRegister() {
        form.clear();

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.renderRegister("", ""))
            );
            assertThat(result.status(), is(OK));
        });
    }

    @Test
    public void testRenderInfo() {
        form.clear();

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.renderInfo())
            );
            assertThat(result.status(), is(OK));
        });
    }
}
