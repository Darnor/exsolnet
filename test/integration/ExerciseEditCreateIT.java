package integration;

import org.junit.Test;

/**
 * Created by tourn on 11.4.16.
 */
public class ExerciseEditCreateIT extends AbstractIntegrationTest{
    @Test
    public void testCreate() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises/create");
            browser.fill("#title").with("Event Foo");
            browser.fill("#content").with("Än neuä Event wo i erstell.");
            browser.fill("#othertag-filter-list").with("MinTäg, DinTäg");
            browser.fill("#maintag-filter-list").with("An1I");
            browser.submit("#save");

        });
    }
}
