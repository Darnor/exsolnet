package integration;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LoginLogoutIT extends AbstractIntegrationTest {
    @Test
    public void integrationTest() {
        //as method includes login
        as("testhansli", browser -> {
            browser.goTo("/login");
            assertThat(browser.pageSource(), containsString("testhansli"));
            browser.submit("#btn_logout");
            browser.goTo("/dashboard");
            assertThat(browser.url(), containsString("/login"));
            assertEquals(null, browser.getCookie("PLAY_SESSION"));
        });
    }
}
