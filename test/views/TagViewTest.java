package views;

import models.User;
import org.junit.Before;
import org.junit.Test;
import play.twirl.api.Content;
import repositories.AbstractRepositoryTest;
import repositories.TagRepository;
import repositories.UserRepository;
import views.html.tagList;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by revy on 07.04.16.
 */
public class TagViewTest extends AbstractRepositoryTest {
    private TagRepository tagRepository;
    private User testUser;

    @Before
    public void setUp() {
        tagRepository = new TagRepository();
        testUser = new UserRepository().find().byId(8000l);
    }

    @Test
    public void testTagExist() {
        Content html = tagList.render(testUser.getEmail(), tagRepository.find().all(), tagRepository.getTrackedTags(testUser), 1, null);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Mathematics"));
        assertThat(html.body(), containsString("Software Engineering"));
    }
}
