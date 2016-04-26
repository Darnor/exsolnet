package integration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import play.Logger;

import java.util.concurrent.TimeUnit;

import static helper.RegexMatcher.matches;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.FIREFOX;

public class ExerciseListIT extends AbstractIntegrationTest{
    @Test
    public void testNoFilterExercises() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            Logger.info(browser.pageSource());
            Logger.info(browser.pageSource());
            assertThat(browser.pageSource(), matches("Ableitung 1a"));
            assertThat(browser.pageSource(), matches("An2I.*Ableiten.*Funktioin.*Blubberduck.*1.*3.*02.12.15 08:00"));
        });
    }

    @Test
    public void testGoToExercisesViaHeader() {
        as(FRANZ, browser -> {
            browser.click("a",withText("Aufgaben"));
            browser.await().atMost(1, TimeUnit.SECONDS).untilPage().isLoaded();
            assertThat(browser.pageSource(), matches("Ableitung 1a"));
            assertThat(browser.pageSource(), matches("An2I.*Ableiten.*Funktion.*Blubberduck"));
        });
    }

    @Test
    public void testTitleFilterForm() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            browser.fill("#title-filter").with("XXX");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*Allgemein.*Hans"));
        });
    }

    @Test
    public void testTagFilterForm() {
        as(FRANZ, FIREFOX, browser -> {
            browser.goTo("/exercises");
            WebElement mainTagElement = browser.getDriver().findElement(By.id("token-input-tag-filter-list"));
            fillTagTokenElements(browser, mainTagElement, "Allgemein");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*Allgemein.*Hans"));
        });
    }

    @Test
    public void testTagAndFilterForm() {
        as(FRANZ, FIREFOX, browser -> {
            browser.goTo("/exercises");
            browser.fill("#title-filter").with("XXX");
            WebElement mainTagElement = browser.getDriver().findElement(By.id("token-input-tag-filter-list"));
            fillTagTokenElements(browser, mainTagElement, "Allgemein");
            browser.submit("#submit-filter");
            assertThat(browser.pageSource(), matches("XXX.*Allgemein.*Hans"));
        });
    }

    @Test
    public void testSortTitleDesc() {
        as(FRANZ, browser -> {
            browser.goTo("/exercises");
            browser.click("a",withText("Titel"));
            browser.await().atMost(1, TimeUnit.SECONDS).untilPage().isLoaded();
            assertThat(browser.pageSource(), matches("XXX.*Allgemein.*Hans"));
        });
    }
}
