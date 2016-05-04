package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.junit.Assert.assertThat;

public class DashboardIT extends AbstractIntegrationTest {
    @Test
    public void test() {
        as(FRANZ, browser -> {
            browser.goTo("/user");
            assertThat(browser.pageSource(), matches("An1I.*1/1"));
            assertThat(browser.pageSource(), matches("SE2.*0/5"));
        });
    }
}
