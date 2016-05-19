package integration;

import helper.AbstractApplicationTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import play.test.TestBrowser;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static play.test.Helpers.*;

public abstract class AbstractIntegrationTest extends AbstractApplicationTest {
    public static final String FRANZ = "Franz";

    public static void as(String username, final Consumer<TestBrowser> block){
        as(username, HTMLUNIT, block);
    }

    public static void as(String username, Class<? extends WebDriver> driver, final Consumer<TestBrowser> block){
        running(testServer(3333, fakeApplication()), driver, browser -> {
            browser.goTo("http://localhost:3333/");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();
            browser.fill("#email").with(username);
            browser.submit("#btn_login");
            browser.await().atMost(2, TimeUnit.SECONDS).untilPage().isLoaded();
            block.accept(browser);
        });
    }

    protected static void fillTagTokenElements(TestBrowser browser, WebElement element, String tag) {
        element.sendKeys(tag);
        browser.await().atMost(2, TimeUnit.SECONDS).until(".token-input-selected-dropdown-item-facebook").isPresent();
        element.sendKeys(Keys.ENTER);
    }

    protected static void fillCKEditor(TestBrowser browser, String content, String editorTyp) {
        WebDriver driver = browser.getDriver();
        WebElement iframe = driver.findElement(By.id(editorTyp)).findElement(By.tagName("iframe"));
        driver.switchTo().frame(iframe);
        WebElement contentInput = driver.findElement(By.tagName("body"));
        contentInput.clear();
        contentInput.sendKeys(content);
        driver.switchTo().parentFrame();
    }
}
