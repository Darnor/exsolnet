package integration;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static helper.RegexMatcher.matches;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.FIREFOX;

public class TagListIT extends AbstractIntegrationTest {

    private static final String TRACK_STR = "Folgen";
    private static final String UNTRACK_STR = "Nicht mehr folgen";
    private static final String TAGS_PATH = "/tags";
    private static final long AD1_TAG_ID = 8002L;
    private static final long AD2_TAG_ID = 8004L;

    @Test
    public void testTrackUntrack() {
        as(FRANZ,FIREFOX, browser -> {
            browser.goTo(TAGS_PATH);
            assertThat(browser.pageSource(), is(matches("input.*" + AD2_TAG_ID + ".*" + TRACK_STR)));
            browser.click("#track_" + AD2_TAG_ID);

            browser.await().atMost(2, TimeUnit.SECONDS).until("#track_" + AD2_TAG_ID).hasAttribute("value", UNTRACK_STR);

            assertThat(browser.pageSource(), is(matches("input.*" + AD2_TAG_ID + ".*" + UNTRACK_STR)));
            browser.click("#track_" + AD2_TAG_ID);

            browser.await().atMost(2, TimeUnit.SECONDS).until("#track_" + AD2_TAG_ID).hasAttribute("value", TRACK_STR);

            assertThat(browser.pageSource(), is(matches("id=\"track_" + AD2_TAG_ID + ".*value=\"" + TRACK_STR)));
        });
    }

    @Test
    public void filterTagsWithAD2() {
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            browser.fill("#tagfilter").with("AD2");
            browser.submit("#submit-tagfilter");
            assertThat(browser.pageSource(), is(matches("input.*" + AD2_TAG_ID + ".*" + TRACK_STR)));
            assertThat(browser.pageSource(), is(not(matches("input.*" + AD1_TAG_ID + ".*" + TRACK_STR))));
        });
    }

    @Test
    public void testIsSortedByNameByDefault(){
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            assertThat(browser.pageSource(), matches("Ableiten.*AD1.*AD2.*Allgemein"));
        });
    }

    @Test
    public void testSortByNameReverse(){
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            browser.find("a", withText("Name")).click();
            browser.await().atMost(1, TimeUnit.SECONDS).untilPage().isLoaded();
            assertThat(browser.pageSource(), matches("Allgemein.*AD2.*AD1.*Ableiten"));
        });
    }

    @Test
    public void testSortByExerciseCount(){
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            browser.click("#header_Aufgaben");
            browser.await().atMost(1, TimeUnit.SECONDS).untilPage().isLoaded();
            assertThat(browser.pageSource(), matches("5.*3.*2.*1"));
        });
    }

    @Test
    public void testSortByTracking(){
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            browser.find("a", withText("Status")).click();
            browser.await().atMost(1, TimeUnit.SECONDS).untilPage().isLoaded();
            assertThat(browser.pageSource(), matches("Nicht mehr folgen.*Nicht mehr folgen.*Folgen.*Folgen.*Folgen"));
        });
    }
}
