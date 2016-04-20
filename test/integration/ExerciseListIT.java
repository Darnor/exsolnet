package integration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static helper.RegexMatcher.matches;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.junit.Assert.assertThat;

public class ExerciseListIT extends AbstractIntegrationTest{
    private void fillTagTokenElements(WebElement element, String tag) {
        try {
            element.sendKeys(tag);
            //TODO Beter way to input tags... Thread sleep -> workaround...
            // Wait 2 seconds for the Token to be created. Very brittle for tests...
            Thread.sleep(2000);
            element.sendKeys(Keys.ENTER);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testNoFilterExercises() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            assertThat(browser.pageSource(), matches("Ableitung 1a"));
            assertThat(browser.pageSource(), matches("von.*Blubberduck.*An2I.*Ableiten.*Funktion"));
        });
    }

    @Test
    public void testTitleFilterForm() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            browser.fill("#title-filter").with("XXX");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*von.*Hans.*Allgemein"));
        });
    }

    @Test
    public void testTagFilterForm() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            WebElement mainTagElement = browser.getDriver().findElement(By.id("token-input-tag-filter-list"));
            fillTagTokenElements(mainTagElement, "Allgemein");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*von.*Hans.*Allgemein"));
        });
    }

    @Test
    public void testTagAndFilterForm() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            browser.fill("#title-filter").with("XXX");
            WebElement mainTagElement = browser.getDriver().findElement(By.id("token-input-tag-filter-list"));
            fillTagTokenElements(mainTagElement, "Allgemein");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*von.*Hans.*Allgemein"));
        });
    }

    @Test
    public void testSortTitleDesc() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            browser.click("a",withText("Titel"));
            assertThat(browser.pageSource(), matches("XXX.*von.*Hans.*Allgemein"));
        });
    }
}
