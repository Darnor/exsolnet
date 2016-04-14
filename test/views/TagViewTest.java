package views;

import models.AbstractModelTest;
import models.Tag;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.twirl.api.Content;
import views.html.tagList;

import java.util.ArrayList;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by revy on 07.04.16.
 */
public class TagViewTest extends AbstractModelTest {

    private User testUser;

    @Before
    public void setUp() {
        testUser = User.findUser("Franz");
    }

    @Test
    public void testTagExist() {
        Content html = tagList.render("", Tag.find().all(), new ArrayList<>(), 1, null);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Mathematics"));
        assertThat(html.body(), containsString("Software Engineering"));
    }

    @Test
    public void testTagIsTracked() {
        Content html = tagList.render(testUser.getEmail(), Tag.find().all(), testUser.getTrackedTags(), 1, null);
        assertEquals("text/html", html.contentType());
    }
}
