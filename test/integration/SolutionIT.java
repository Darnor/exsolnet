package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.FIREFOX;

public class SolutionIT extends AbstractIntegrationTest {

    @Test
    public void testEditSolution() {
        as(FRANZ, FIREFOX, browser -> {
            String content = "Dies ist ein Test.";
            browser.goTo("/exercises/8000");
            browser.click("a",withId("solution-8001"));

            browser.await().untilPage().isLoaded();

            fillCKEditor(browser, content);

            browser.submit("#save");

            assertThat(browser.pageSource(), matches(content+"*.Zuletzt geändert:"));
        });
    }
}
