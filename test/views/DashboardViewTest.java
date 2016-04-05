package views;

import models.Exercise;
import models.Tag;
import org.junit.Test;
import play.twirl.api.Content;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by tourn on 4.4.16.
 */
public class DashboardViewTest {
    @Test
    public void renderTemplate() {
        ArrayList<Tag> list = new ArrayList<>();
        Tag t1 = new Tag();
        t1.setName("One");
        Tag t2 = new Tag();
        t2.setName("Two");
        list.add(t1);
        list.add(t2);
        Content html = views.html.dashboard.render("Franz", list, null);
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Franz"));
        assertThat(html.body(), containsString("<li>One (0/0)</li>"));
        assertThat(html.body(), containsString("<li>Two (0/0)</li>"));
    }
}
