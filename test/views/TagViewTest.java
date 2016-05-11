package views;

import models.Tag;
import models.Tracking;
import models.User;
import models.builders.TagBuilder;
import models.builders.TrackingBuilder;
import models.builders.UserBuilder;
import org.junit.Test;
import play.twirl.api.Content;
import views.html.tagList;

import java.util.ArrayList;
import java.util.Arrays;

import static helper.RegexMatcher.matches;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TagViewTest extends AbstractViewTest{

    @Test
    public void testTagExist() {
        Tag aTag = TagBuilder.aTag().withName("Mathematics").build();
        Tag bTag = TagBuilder.aTag().withName("Software Engineering").build();
        Content html = tagList.render(UserBuilder.anUser().withUsername("Hans").build(), Arrays.asList(aTag, bTag), new ArrayList<>(), 1, "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Mathematics"));
        assertThat(html.body(), containsString("Software Engineering"));
    }

    @Test
    public void testTagIsTracked() {
        Tag aTag = TagBuilder.aTag().withName("Mathematics").withId(1L).build();
        Tag bTag = TagBuilder.aTag().withName("Software Engineering").withId(2L).build();

        User testUser = UserBuilder.anUser().withEmail("Peter").withUsername("Peter").withId(1L).build();

        Tracking aTracking = TrackingBuilder.aTracking().withId(1L).withUser(testUser).withTag(aTag).build();

        testUser.setTrackings(Arrays.asList(aTracking));

        Content html = tagList.render(testUser, Arrays.asList(aTag, bTag), testUser.getTrackedTags(), 1, "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), is(matches("<input type=\"submit\".*value=\"Nicht mehr folgen\".*class=\"btn btn-success btn-block.*id=\"track_1\".*/>")));
        assertThat(html.body(), is(matches("<input type=\"submit\".*value=\"Folgen\".*class=\"btn btn-primary btn-block\".*id=\"track_2\".*/>")));
    }
}
