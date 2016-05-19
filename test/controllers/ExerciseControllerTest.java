package controllers;

import helper.AbstractApplicationTest;
import models.Exercise;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;
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

    private void testProcessCreateFormFail(Map<String, String> form) {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processCreate())
                            .bodyForm(form)
                            .session("connected", "blubberduck@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/create"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testProcessCreateFormValidationEmptyTitle() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "");
        form.put("content", "a");
        form.put("contentsol", "a");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationEmptyContent() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "");
        form.put("contentsol", "a");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationPTagContent() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "<p>   </p>");
        form.put("contentsol", "a");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationPTagNewLineContent() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "<p>   \n      \n     \r\n     \n   </p>");
        form.put("contentsol", "a");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationNoPTagNewLineContent() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "   \n      \n     \r\n     \n   ");
        form.put("contentsol", "a");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationEmptySolutionContent() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "a");
        form.put("contentsol", "");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationNoMainTag() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "a");
        form.put("contentsol", "a");
        form.put("maintag", "");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationOnlyScriptTag() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "<script>alert(\"Hello from the DB side\"</script>");
        form.put("contentsol", "a");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormValidationSolutionOnlyScriptTag() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "a");
        form.put("contentsol", "<script>alert(\"Hello from the DB side\"</script>");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessCreateFormFail(form);
    }

    @Test
    public void testProcessCreateFormWithScriptTag() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "scripting alert");
        form.put("content", "nothing to see here.<script>alert(\"Hello from the DB side\")</script>");
        form.put("contentsol", "blub");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processCreate())
                            .bodyForm(form)
                            .session("connected", "franz@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises"));
            assertThat(result.status(), is(SEE_OTHER));
            assertThat(Exercise.find().all().stream().filter(e -> e.getTitle().equals("scripting alert") && e.getContent().equals("nothing to see here.")).count(), is(1L));
        });
    }

    private void testProcessUpdateFormFail(Map<String, String> form, long id) {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processUpdate(id))
                            .bodyForm(form)
                            .session("connected", "blubberduck@hsr.ch")
            );
            Optional<String> location = result.redirectLocation();
            assertTrue(location.isPresent());
            assertThat(location.get(), is("/exercises/" + id + "/edit"));
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testProcessUpdateFormValidationOnlyScriptTag() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "<script>alert(\"Hello from the DB side\"</script>");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessUpdateFormFail(form, 8000);
    }

    @Test
    public void testProcessUpdateFormValidationNoTitle() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "");
        form.put("content", "asdf");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessUpdateFormFail(form, 8000);
    }

    @Test
    public void testProcessUpdateInvalidId() {
        Map<String, String> form = new HashMap<>();
        form.put("title", "a");
        form.put("content", "as");
        form.put("maintag", "AD1");
        form.put("othertag", "");

        testProcessUpdateFormFail(form, -1);
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
    public void testModeratorDeleteNonExisting() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.ExerciseController.processDelete(-1L))
                            .session("connected", "blubberduck@hsr.ch")
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }

    @Test
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
