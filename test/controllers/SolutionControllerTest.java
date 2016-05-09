package controllers;

import helper.AbstractApplicationTest;
import org.junit.Test;
import play.mvc.Result;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static play.test.Helpers.*;

public class SolutionControllerTest extends AbstractApplicationTest {

    @Test
    public void testUnauthorizedOnUnallowedDeleteAttempt() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processDelete(8000L))
                            .session("connected", "mario@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testModeratorDelete() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processDelete(8002L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testOkOnAllowedDeleteAttemptFromOwner() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processDelete(8001L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(SEE_OTHER));
        });
    }
}
