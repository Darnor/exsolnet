import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.*;

@Ignore("Just here for reference")
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
