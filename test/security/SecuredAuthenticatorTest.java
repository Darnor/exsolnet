package security;

import com.google.common.collect.ImmutableMap;
import controllers.routes;
import org.junit.Test;
import play.mvc.*;
import services.SessionService;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.*;
import static org.junit.Assert.assertThat;
import static play.mvc.Controller.session;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;


/**
 * Created by mario on 11.04.16.
 */
public class SecuredAuthenticatorTest {

    @Test
    public void authenticateFailureOnExerciseController(){
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderOverview())
            );
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.redirectLocation().toString(), containsString("/login"));
        });
    }

    @Test
    public void authenticateFailureOnDashboardController(){
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.DashboardController.renderDashboard())
            );
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.redirectLocation().toString(), containsString("/login"));
        });
    }

    @Test
    public void authenticateFailureOnTagController(){
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.TagController.renderOverview())
            );
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.redirectLocation().toString(), containsString("/login"));
        });
    }

    @Test
    public void grantAccesWhenAuthenticatedExerciseController(){
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderOverview())
                    .session("connected", "ursli")
            );
            assertThat(result.status(), is(OK));
        });
    }


    @Test
    public void authenticateWithUserFormData(){
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.LoginController.login())
                            .bodyForm(ImmutableMap.of(
                                    "email", "ursli",
                                    "password", "urslispw"
                                ))
            );
            assertThat(result.session().get(SessionService.KEY_USERNAME), is("ursli"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(result.redirectLocation().toString(), containsString("/"));
        });
    }

}