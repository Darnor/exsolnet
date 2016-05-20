package controllers;

import helper.AbstractApplicationTest;
import org.junit.Test;
import play.mvc.Result;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void testDeleteNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processDelete(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testProcessUpdateNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUpdate(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testRenderUpdateNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.renderUpdate(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testProcessCreateNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processCreate(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testRenderCreateNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.renderCreate(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testNonModeratorDeleteUndo() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUndo(7997L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testNonModeratorDeleteUndoSameUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUndo(7999L))
                            .session("connected", "franz@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/8000"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testModeratorDeleteUndoNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUndo(-1L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testNonModeratorDeleteUndoNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUndo(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testRenderUpdateInvalidId() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUndo(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testUpvoteNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUpvote(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testDownvoteNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processDownvote(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testModeratorDeleteUndo() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.SolutionController.processUndo(7998L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/8002"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

}
