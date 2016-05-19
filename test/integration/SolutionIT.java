package integration;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static helper.RegexMatcher.matches;
import static org.fluentlenium.core.filter.FilterConstructor.withId;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.FIREFOX;

public class SolutionIT extends AbstractIntegrationTest {

    @Test
    public void testEditSolution() {
        as(FRANZ, FIREFOX, browser -> {
            String content = "Dies ist ein Test.";
            browser.goTo("/exercises/8000");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();

            browser.click("a", withId("solution-8001"));
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();

            fillCKEditor(browser, content, "solution_content");
            browser.submit("#save");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();

            assertThat(browser.pageSource(), matches(content + "*.Zuletzt geändert:"));
        });
    }

    @Test
    public void testCreateSolution() {
        as(FRANZ, FIREFOX, browser -> {
            String content = "Dies ist ein Test.";
            browser.goTo("/exercises/8007");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();

            browser.click("a", withText("Aufgabe Lösen"));
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();

            fillCKEditor(browser, content, "solution_content");
            browser.submit("#save");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();

            assertThat(browser.pageSource(), matches(content));
        });
    }
}
