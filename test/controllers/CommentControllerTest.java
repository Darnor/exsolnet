package controllers;

import helper.AbstractApplicationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
            assertThat(result.status(), is(SEE_OTHER));
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
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    @Ignore("Not implemented yet")
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
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    @Ignore("Not implemented yet")
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
    @Ignore("Fix the code!")
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
}
