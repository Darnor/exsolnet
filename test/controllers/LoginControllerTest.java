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
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(session));
        });
    }

    @Test
    public void testRegisterFailedEmptyUserName() {
        form.replace("username", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
                        Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?email=testUser%40test.test"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testRegisterFailedEmptyEmail() {
        form.replace("email", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
                        Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=testUser99"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testRegisterFailedWrongEmailFormat() {
        form.replace("email", "a@a");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=testUser99&email=a%40a"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testRegisterFailedEmptyPassword() {
        form.replace("password", "");
        form.replace("password-check", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
                        Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=testUser99&email=testUser%40test.test"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testRegisterFailedNotTheSamePassword() {
        form.replace("password", "a");
        form.replace("password-check", "ab");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processRegister())
                            .bodyForm(form)
            );
                        Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/register?username=testUser99&email=testUser%40test.test"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(new HashMap<>()));
        });
    }

    @Test
    public void testUserLoginValidEmail() {
        form.clear();
        form.put("emailorusername", "franz@hsr.ch");
        form.put("password", "");

        Map<String, String> session = new HashMap<>();
        session.put("connected", "franz@hsr.ch");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processLogin())
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(session));
        });
    }

    @Test
    public void testUserLoginValidUserName() {
        form.clear();
        form.put("emailorusername", "franz");
        form.put("password", "");

        Map<String, String> session = new HashMap<>();
        session.put("connected", "franz@hsr.ch");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.processLogin())
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.session(), is(session));
        });
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
}
