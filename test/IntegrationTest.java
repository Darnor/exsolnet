import org.junit.*;

import org.junit.runner.RunWith;
import play.mvc.*;
import play.test.*;

import static org.hamcrest.core.StringContains.containsString;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    @Test
    public void test() {
        running(testServer(3333, fakeApplication()), FIREFOX, browser -> {
            browser.goTo("http://localhost:3333/");
            assertThat(browser.pageSource(), containsString("Aufgaben"));
            browser.fill("#username").with("Franz");
            browser.submit("#btn_login");
            assertEquals("Franz", browser.find("#lbl_username").getText());
        });
    }

}
