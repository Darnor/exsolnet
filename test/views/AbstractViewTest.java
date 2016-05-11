package views;

import org.junit.Before;
import org.junit.BeforeClass;
import play.Application;
import play.mvc.Http;
import play.test.Helpers;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.mock;

public abstract class AbstractViewTest {
    public static Application app;
    private final Http.Request request = mock(Http.Request.class);

    @BeforeClass
    public static void startApp() {
        app = Helpers.fakeApplication();
        Helpers.start(app);
    }

    @Before
    public void setUp() throws Exception {
        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> argData = Collections.emptyMap();
        Long id = 2L;
        play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
        Http.Context context = new Http.Context(id, header, request, flashData, flashData, argData);
        Http.Context.current.set(context);
    }
}
