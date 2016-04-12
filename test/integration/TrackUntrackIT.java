package integration;

import models.AbstractModelTest;
import models.Tag;
import models.User;
import org.junit.Before;
import org.junit.Test;

import static integration.AbstractIntegrationTest.FRANZ;
import static integration.AbstractIntegrationTest.as;
import static org.junit.Assert.assertEquals;

/**
 * Created by revy on 12.04.16.
 */
public class TrackUntrackIT extends AbstractModelTest {

    private static final String TRACK_STR = "Folgen";
    private static final String UNTRACK_STR = "Nicht mehr folgen";
    private static final String TAGS_PATH = "/tags";
    private static final long TAG_ID = 8004L;

    private User testUser;
    private Tag testTag;

    @Before
    public void setUp() {
        this.testUser = User.findUser(FRANZ);
        this.testTag = Tag.find().byId(TAG_ID);
    }

    @Test
    public void track() {
        int currentNoOfTrackedTags = testUser.getTrackedTags().size();
        System.out.println(currentNoOfTrackedTags);
        as(FRANZ, browser -> {
            browser.goTo(TAGS_PATH);
            browser.submit("#btn_" + TAG_ID);
            assertEquals(currentNoOfTrackedTags, testUser.getTrackedTags().size());
        });
    }
}
