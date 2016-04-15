package integration;

import models.Exercise;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * Created by tourn on 11.4.16.
 */
public class ExerciseEditCreateIT extends AbstractIntegrationTest {
    @Test
    public void testCreate() {
        as(FRANZ, browser -> {

            String title = "Event Foo";
            String otherTag = "MinTäg, DinTäg";
            String content = "En neue Event wo i erstell.";
            String mainTag = "An1I";

            browser.goTo("/exercises/create");
            browser.fill("#title").with(title);
            browser.fill("#content").with(content);

            WebElement element = browser.getDriver().findElement(By.id("token-input-maintag-filter-list"));
            element.sendKeys(mainTag + Keys.RETURN);
            WebElement element2 = browser.getDriver().findElement(By.id("token-input-othertag-filter-list"));
            element.sendKeys(otherTag + Keys.RETURN);

            browser.submit("#save");

            Exercise newExercise = Exercise.find().where().eq("title", "Event Foo").findUnique();
            Assert.assertEquals(title,newExercise.getTitle());
            Assert.assertEquals(content,newExercise.getContent());
          //TODO shall work  Assert.assertEquals(3,newExercise.getTags().size());
        });
    }

    @Test
    public void testUpdate() {
        as(FRANZ, browser -> {
            Exercise updateExercise = Exercise.find().where().eq("id", 8000L).findUnique();

            String title = updateExercise.getTitle() + " version 2";
            String content = updateExercise.getContent() + " und jetzt no en zuesatz zu dere Ufgab";


            browser.goTo("/exercises/8000/edit");
            browser.fill("#title").with(title);
            browser.fill("#content").with(content);
            browser.submit("#save");

            Exercise newExercise = Exercise.find().where().eq("id", 8000L).findUnique();
            Assert.assertEquals(newExercise.getTitle(), title);
            Assert.assertEquals(newExercise.getContent(), content);
        });
    }
}
