package controllers;

import helper.AbstractApplicationTest;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Result;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    public void testUnauthorizedOnUnallowedDeleteAttempt() {
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
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testUnauthorizedOnUnallowedDeleteAttemptSameUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processDelete(8000L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testNonModeratorDeleteUndo() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processUndo(7999L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    @Ignore("Not implemented yet")
    public void testModeratorDeleteUndoNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processUndo(-1L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testNonModeratorDeleteUndoNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processUndo(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testModeratorDeleteUndo() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processUndo(7998L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/7998"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testRenderOverview() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderOverview())
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(OK));
        });
    }

    @Test
    public void testRenderEditValidExerciseModerator() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderEdit(8000))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(OK));
        });
    }

    @Test
    public void testRenderEditValidExerciseSameUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderEdit(8001))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(OK));
        });
    }

    @Test
    public void testRenderEditValidExerciseOtherUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderEdit(8001))
                            .session("connected", "simon@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testRenderEditInvalidExercise() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderEdit(-1))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    @Ignore("Not implemented")
    public void testRenderEditFakeSession() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderEdit(8000))
                            .session("connected", "l337hax03r@h4xr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/login"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testRenderDetail() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.renderDetail(-1))
                            .session("connected", "simon@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }
}
