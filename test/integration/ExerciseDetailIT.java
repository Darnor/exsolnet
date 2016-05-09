package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.junit.Assert.assertThat;

public class ExerciseDetailIT extends AbstractIntegrationTest{
    @Test
    public void testExerciseExist() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises/8000");
            assertThat(browser.pageSource(), matches("h1.*Grundlegende Mathematik.*/h1"));
        });
    }
}
