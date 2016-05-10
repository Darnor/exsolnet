package controllers;

import helper.AbstractApplicationTest;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static play.test.Helpers.*;

public class TagControllerTest extends AbstractApplicationTest {

    Map<String, String> sessionMap;

    @Before
    public void setUp() {
        sessionMap = new HashMap<>();
        sessionMap.put("connected", "franz@hsr.ch");
        sessionMap.put("tagFilter", "");
        sessionMap.put("tagOrder", "1");
    }

    @Test
    public void testExistingTagTrack() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.TagController.processTrack(8000))
                            .session(sessionMap)
            );
            assertThat(result.status(), is(SEE_OTHER));
        });
    }

    @Test
    public void testNonExistingTagTrack() {
        running(fakeApplication(), () -> {
            Result result = route(
                    fakeRequest(routes.TagController.processTrack(-1))
                            .session(sessionMap)
            );
            assertThat(result.status(), is(NOT_FOUND));
        });
    }
}
