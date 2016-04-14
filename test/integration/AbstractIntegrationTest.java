package integration;

import models.AbstractModelTest;
import play.test.TestBrowser;

import java.util.function.Consumer;

import static org.junit.Assert.assertNotEquals;
import static play.test.Helpers.*;

public abstract class AbstractIntegrationTest {
    public static final String FRANZ = "Franz";

    public static void as(String username, final Consumer<TestBrowser> block){
        running(testServer(3333, fakeApplication()), FIREFOX, browser -> {
            AbstractModelTest.cleanDB();
            browser.goTo("http://localhost:3333/");
            browser.fill("#email").with(username);
            browser.submit("#btn_login");
            assertNotEquals("Login failed!", "/login", browser.url());
            System.out.println(browser.url());
            block.accept(browser);
        });

    }

}
