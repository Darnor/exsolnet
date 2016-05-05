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
                    fakeRequest(routes.ExerciseController.delete(8000L))
                            .session("connected", "Mario")
            );
            assertThat(result.body().dataStream().toString(), is("not allowed"));
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void

}
