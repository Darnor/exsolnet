package integration;

import models.Exercise;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.hamcrest.core.StringContains.containsString;
import static play.test.Helpers.FIREFOX;

public class ExerciseEditCreateIT extends AbstractIntegrationTest {

    @Test
    public void testCreate() {
        as(FRANZ, FIREFOX, browser -> {
            String title = "Event Foo";
            String mainTag = "An1I" ;
            String[] otherTags = {"Tree", "Pattern"};
            String content = "En neue Event wo i erstell.";

            browser.goTo("/exercises/create");

            browser.fill("#title").with(title);

            Select dropdownmain = new Select(browser.getDriver().findElement(By.id("maintag")));
            dropdownmain.selectByValue(mainTag);


            Select dropdownother = new Select(browser.getDriver().findElement(By.id("othertag")));
            for (String tag : otherTags) {
                dropdownother.selectByValue(tag);
            }

            fillCKEditor(browser, content);

            browser.submit("#save");

            Exercise newExercise = Exercise.find().where().eq("title", title).findUnique();
            Assert.assertNotNull(newExercise);
            Assert.assertEquals(title,newExercise.getTitle());
            Assert.assertThat(newExercise.getContent(),containsString(content));
            Assert.assertEquals(3,newExercise.getTags().size());
        });
    }

    @Test
    public void testUpdate() {
        as(FRANZ, FIREFOX, browser -> {
            Exercise updateExercise = Exercise.find().where().eq("id", 8000L).findUnique();

            Assert.assertNotNull(updateExercise);

            String title = updateExercise.getTitle() + " version 2";
            String content = "und jetzt no en zuesatz zu dere Ufgab";


            browser.goTo("/exercises/8000/edit");
            browser.fill("#title").with(title);
            fillCKEditor(browser, content);
            browser.submit("#save");

            Exercise newExercise = Exercise.find().where().eq("id", 8000L).findUnique();

            Assert.assertNotNull(newExercise);
            Assert.assertEquals(newExercise.getTitle(), title);
            Assert.assertThat(newExercise.getContent(),containsString(content));
        });
    }
}
