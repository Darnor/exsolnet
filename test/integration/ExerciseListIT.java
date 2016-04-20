package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.junit.Assert.assertThat;

public class ExerciseListIT extends AbstractIntegrationTest{
    @Test
    public void testNoFilterExercises() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            assertThat(browser.pageSource(), matches("Ableitung 1a"));
            assertThat(browser.pageSource(), matches("von.*Blubberduck.*An2I.*Ableiten.*Funktion"));
        });
    }
}
