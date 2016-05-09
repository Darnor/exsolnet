package controllers;

import helper.AbstractApplicationTest;
import org.junit.Test;
import play.mvc.Result;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class ExerciseControllerTest extends AbstractApplicationTest {

    @Test
    public void testGetOrderByAttributeString() {
        assertEquals("title", ExerciseController.getOrderByAttributeString(1));
        assertEquals("solutionCount", ExerciseController.getOrderByAttributeString(2));
        assertEquals("points", ExerciseController.getOrderByAttributeString(3));
        assertEquals("time", ExerciseController.getOrderByAttributeString(4));
    }

    @Test
    public void testUnauthorizedOnUnallowedDeleteAtempt() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processDelete(8000L))
                            .session("connected", "mario@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testModeratorDelete() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processDelete(8002L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(PERMANENT_REDIRECT));
        });
    }

    @Test
    public void testUnauthorizedOnUnallowedDeleteAtemptSameUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processDelete(8000L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }
}
