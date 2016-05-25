package controllers;

import helper.AbstractApplicationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static helper.RegexMatcher.matches;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

public class CommentControllerTest extends AbstractApplicationTest {

    Map<String, String> form;

    @Before
    public void setUp() {
        form = new HashMap<>();
        form.put("content", "comment from franz");
    }

    @After
    public void cleanUp() {
        form = null;
    }


    @Test
    public void testCreateExerciseComment() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateExerciseComment(8000))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), matches("/exercises/8000"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testCreateSolutionCommentInvalidIdNonModerator() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateSolutionComment(-1))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testCreateSolutionCommentInvalidIdModerator() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateSolutionComment(-1))
                            .session("connected", "blubberduck@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testCreateExerciseCommentInvalidIdNonModerator() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateExerciseComment(-1))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testCreateExerciseCommentInvalidIdModerator() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateExerciseComment(-1))
                            .session("connected", "blubberduck@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testAuthorizedCreateSolutionComment() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateSolutionComment(8002))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), matches("/exercises/8001"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testUnauthorizedCreateSolutionComment() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processCreateSolutionComment(8005))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testAuthorizedCommentUpdate() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUpdate(8001))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/8000"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testUnuthorizedCommentUpdate() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUpdate(8002))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testUnknownExerciseOrSolutionCommentUpdate() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUpdate(-1))
                            .session("connected", "franz@hsr.ch")
                            .bodyForm(form)
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }
    @Test
    public void testUnauthorizedOnUnallowedDeleteAttempt() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processDelete(8000L))
                            .session("connected", "mario@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testModeratorDelete() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processDelete(8002L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/8002"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testAllowedDeleteAttemptSameUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processDelete(8000L))
                            .session("connected", "hans@hsr.ch")
            );
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testNonModeratorDeleteUndo() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUndo(8000L))
                            .session("connected", "hans@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/8000"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testDeleteNonExistingComment() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processDelete(-1L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testUndoDeleteOtherUser() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUndo(8006L))
                            .session("connected", "franz@hsr.ch")
            );
            assertThat(result.status(), is(UNAUTHORIZED));
        });
    }

    @Test
    public void testModeratorDeleteUndoNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUndo(-1L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
    public void testModeratorDeleteUndo() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.CommentController.processUndo(8003L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/8002"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }
}
