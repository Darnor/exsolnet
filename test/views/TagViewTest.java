package views;

import models.Tag;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.twirl.api.Content;
import models.AbstractModelTest;
import models.TagRepository;
import models.UserRepository;
import views.html.tagList;

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
        //FIXME create user objects instead of pulling them from DB - decouple view tests from database
        testUser = User.find().byId(8000l);
    }

    @Test
    public void testTagExist() {
        Content html = tagList.render(testUser.getEmail(), Tag.find().all(), testUser.getTrackedTags(), 1, null);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Mathematics"));
        assertThat(html.body(), containsString("Software Engineering"));
    }
}
