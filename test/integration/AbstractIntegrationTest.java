package integration;

import helper.AbstractApplicationTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import play.test.TestBrowser;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.assertNotEquals;
import static play.test.Helpers.*;

public abstract class AbstractIntegrationTest extends AbstractApplicationTest {
    public static final String FRANZ = "Franz";

    public static void as(String username, final Consumer<TestBrowser> block){
        running(testServer(3333, fakeApplication()), FIREFOX, browser -> {
            browser.goTo("http://localhost:3333/");
            browser.fill("#email").with(username);
            browser.submit("#btn_login");
            assertNotEquals("Login failed!", "/login", browser.url());
            block.accept(browser);
        });

    }

    protected static void fillTagTokenElements(TestBrowser browser, WebElement element, String tag) {
        element.sendKeys(tag);
        browser.await().atMost(2, TimeUnit.SECONDS).until(".token-input-selected-dropdown-item-facebook").isPresent();
        element.sendKeys(Keys.ENTER);
    }

}
