package integration;

import helper.AbstractApplicationTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import play.Logger;
import play.test.TestBrowser;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static play.test.Helpers.*;

abstract class AbstractIntegrationTest extends AbstractApplicationTest {
    static final String FRANZ = "Franz";

    static void as(String username, Class<? extends WebDriver> driver, final Consumer<TestBrowser> block) {
        running(testServer(3333, fakeApplication()), driver, browser -> {
            do {
                try {
                    browser.goTo("http://localhost:3333/");
                    Logger.debug("Connecting as user: " + username);
                    browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();
                    browser.fill("#email").with(username);
                    browser.fill("#password").with("a");
                    browser.submit("#btn_login");
                    browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();
                    //assertNotEquals("Login failed!", "/login", browser.url());
                    block.accept(browser);
                } catch (Exception exception) {
                    //ignore
                }
            } while (browser.url().equals("/login"));
        });
    }

    static void as_noretry(String username, Class<? extends WebDriver> driver, final Consumer<TestBrowser> block) {
        running(testServer(3333, fakeApplication()), driver, browser -> {

            browser.goTo("http://localhost:3333/");
            Logger.debug("Connecting as user: " + username);
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();
            browser.fill("#email").with(username);
            browser.fill("#password").with("a");
            browser.submit("#btn_login");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();
            //assertNotEquals("Login failed!", "/login", browser.url());
            block.accept(browser);

        });
    }


    static void fillTagTokenElements(TestBrowser browser, WebElement element, String tag) {
        element.sendKeys(tag);
        browser.await().atMost(2, TimeUnit.SECONDS).until(".token-input-selected-dropdown-item-facebook").isPresent();
        element.sendKeys(Keys.ENTER);
    }

    static void fillCKEditor(TestBrowser browser, String content, String editorTyp) {
        WebDriver driver = browser.getDriver();
        WebElement iframe = driver.findElement(By.id(editorTyp)).findElement(By.tagName("iframe"));
        driver.switchTo().frame(iframe);
        WebElement contentInput = driver.findElement(By.tagName("body"));
        contentInput.clear();
        contentInput.sendKeys(content);
        driver.switchTo().parentFrame();
    }
}
