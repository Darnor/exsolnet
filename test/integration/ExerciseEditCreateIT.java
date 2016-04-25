package integration;

import models.Exercise;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static play.test.Helpers.FIREFOX;

public class ExerciseEditCreateIT extends AbstractIntegrationTest {

    @Test
    @Ignore("Claudia pls fix")
    public void testCreate() {
        as(FRANZ, FIREFOX, browser -> {
            String title = "Event Foo";
            String[] mainTags = { "An1I" };
            String[] otherTags = {"MinTäg", "DinTäg"};
            String content = "En neue Event wo i erstell.";

            browser.goTo("/exercises/create");

            browser.fill("#title").with(title);

            WebElement mainTagElement = browser.getDriver().findElement(By.id("token-input-maintag-filter-list"));
            for (String tag : mainTags) {
                fillTagTokenElements(browser, mainTagElement, tag);
            }

            WebElement otherTagElement = browser.getDriver().findElement(By.id("token-input-othertag-filter-list"));
            for (String tag : otherTags) {
                fillTagTokenElements(browser, otherTagElement, tag);
            }

            browser.fill("#content").with(content);
            browser.submit("#save");

            Exercise newExercise = Exercise.find().where().eq("title", title).findUnique();
            Assert.assertNotNull(newExercise);
            Assert.assertEquals(title,newExercise.getTitle());
            Assert.assertEquals(content,newExercise.getContent());
            Assert.assertEquals(3,newExercise.getTags().size());
        });
    }

    @Test
    @Ignore("Claudia pls fix")
    public void testUpdate() {
        as(FRANZ, FIREFOX, browser -> {
            Exercise updateExercise = Exercise.find().where().eq("id", 8000L).findUnique();

            Assert.assertNotNull(updateExercise);

            String title = updateExercise.getTitle() + " version 2";
            String content = updateExercise.getContent() + " und jetzt no en zuesatz zu dere Ufgab";


            browser.goTo("/exercises/8000/edit");
            browser.fill("#title").with(title);
            browser.fill("#content").with(content);
            browser.submit("#save");

            Exercise newExercise = Exercise.find().where().eq("id", 8000L).findUnique();

            Assert.assertNotNull(newExercise);
            Assert.assertEquals(newExercise.getTitle(), title);
            Assert.assertEquals(newExercise.getContent(), content);
        });
    }
}
