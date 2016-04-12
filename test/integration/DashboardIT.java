package integration;

import org.junit.Test;

import static integration.AbstractIntegrationTest.FRANZ;
import static integration.AbstractIntegrationTest.as;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * Created by tourn on 11.4.16.
 */
public class DashboardIT {
    @Test
    public void test() {
        as(FRANZ, browser -> {
            browser.goTo("/dashboard");
            assertThat(browser.pageSource(), containsString("An1I (1/1)"));
            assertThat(browser.pageSource(), containsString("SE2 (0/5)"));

            assertThat(browser.pageSource(), containsString("HOW CAN YOU BE SO WRONG!"));
        });
    }
}
