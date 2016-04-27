package integration;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.FIREFOX;

public class LoginLogoutIT extends AbstractIntegrationTest {
    @Test
    public void integrationTest() {
        //as method includes login
        as("Franz", FIREFOX, browser -> {
            browser.goTo("/login");
            assertThat(browser.pageSource(), containsString("Franz"));
            browser.click("#btn_logout");
            browser.goTo("/");
            assertThat(browser.url(), containsString("/login"));
            assertEquals(null, browser.getCookie("PLAY_SESSION"));
        });
    }
}
