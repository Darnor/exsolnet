package integration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static helper.RegexMatcher.matches;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.junit.Assert.assertThat;

public class ExerciseListIT extends AbstractIntegrationTest{
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
            fillTagTokenElements(browser, mainTagElement, "Allgemein");
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
            fillTagTokenElements(browser, mainTagElement, "Allgemein");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*von.*Hans.*Allgemein"));
        });
    }

    @Test
    public void testSortTitleDesc() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            browser.click("a",withText("Titel"));
            browser.await().atMost(1, TimeUnit.SECONDS).untilPage().isLoaded();
            assertThat(browser.pageSource(), matches("XXX.*von.*Hans.*Allgemein"));
        });
    }
}
