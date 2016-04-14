package integration;

import org.junit.Test;

import static helper.RegexMatcher.matches;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TrackUntrackIT extends AbstractIntegrationTest {

    private static final String TRACK_STR = "Folgen";
    private static final String UNTRACK_STR = "Nicht mehr folgen";
    private static final String TAGS_PATH = "/tags";
    private static final long AD1_TAG_ID = 8002L;
    private static final long AD2_TAG_ID = 8004L;

    @Test
    public void track() {
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            assertThat(browser.pageSource(), is(matches("<input type=\"submit\" id=\"track_" + AD2_TAG_ID + "\" class=\"btn btn-primary btn-block\" value=\"" + TRACK_STR +"\" />")));
            browser.submit("#track_" + AD2_TAG_ID);
            assertThat(browser.pageSource(), is(matches("<input type=\"submit\" id=\"track_" + AD2_TAG_ID + "\" class=\"btn btn-success btn-block\" value=\"" + UNTRACK_STR + "\" />")));
        });
    }

    @Test
    public void filterTagsWithAD2() {
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            browser.fill("#tagfilter").with("AD2");
            browser.submit("#submit-tagfilter");
            assertThat(browser.pageSource(), is(matches("<input type=\"submit\" id=\"track_" + AD2_TAG_ID + "\" class=\"btn btn-primary btn-block\" value=\"" + TRACK_STR +"\" />")));
            assertThat(browser.pageSource(), is(not(matches("<input type=\"submit\" id=\"track_" + AD1_TAG_ID + "\" class=\"btn btn-primary btn-block\" value=\"" + TRACK_STR +"\" />"))));
        });
    }
}
