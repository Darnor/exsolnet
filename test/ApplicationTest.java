import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.avaje.ebean.Model;
import controllers.HomeController;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import repositories.ExerciseRepository;
import models.Exercise;
import org.junit.*;

import play.test.*;
import play.twirl.api.Content;
import services.SessionService;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static play.inject.Bindings.bind;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest extends WithApplication{

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .build();
    }

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void renderTemplate() {
        ArrayList<Exercise> list = new ArrayList<>();
        Content html = views.html.index.render(list, "Franz");
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("Franz"));
    }

    @Test
    public void testHomeController(){
        ExerciseRepository execiseRepositoryMock = mock(ExerciseRepository.class);
        Model.Finder<Long,Exercise> exerciseFinderMock = mock(Model.Finder.class);
        SessionService sessionServiceMock = mock(SessionService.class);


        when(sessionServiceMock.getSession(anyString())).thenReturn("TestUser");

        ArrayList<Exercise> testList = new ArrayList<Exercise>();
        Exercise ex = new Exercise();
        ex.setTitle("test");
        ex.setContent("test");
        ex.setId(0l);
        testList.add(ex);

        when(exerciseFinderMock.all()).thenReturn(testList);
        execiseRepositoryMock.find=exerciseFinderMock;


        HomeController homeController = new HomeController(execiseRepositoryMock, sessionServiceMock);
        Result result = homeController.index();
        assertEquals(200, result.status());
    }




}
