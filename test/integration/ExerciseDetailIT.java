package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.junit.Assert.assertThat;

/**
 * Created by tourn on 11.4.16.
 */
public class ExerciseDetailIT extends AbstractIntegrationTest{
    @Test
    public void test() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises/8000");
            assertThat(browser.pageSource(), matches("h1.*Grundlegende Mathematik.*/h1"));
        });
    }
}
