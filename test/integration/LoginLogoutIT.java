package integration;


import org.junit.Test;

import static integration.AbstractIntegrationTest.as;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by mario on 11.04.16.
 */
public class LoginLogoutIT {
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
