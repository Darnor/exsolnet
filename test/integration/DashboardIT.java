package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static integration.AbstractIntegrationTest.FRANZ;
import static integration.AbstractIntegrationTest.as;
import static org.junit.Assert.assertThat;

/**
 * Created by tourn on 11.4.16.
 */
public class DashboardIT {
    @Test
    public void test() {
        as(FRANZ, browser -> {
            browser.goTo("/dashboard");
            assertThat(browser.pageSource(), matches("An1I.*1/1"));
            assertThat(browser.pageSource(), matches("SE2.*0/5"));

            assertThat(browser.pageSource(), matches("HOW CAN YOU BE SO WRONG!.*Franz.*8000.*Basic Math"));
        });
    }
}
