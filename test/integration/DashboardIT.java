package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.junit.Assert.assertThat;

public class DashboardIT extends AbstractIntegrationTest {
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
