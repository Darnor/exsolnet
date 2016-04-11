package integration;

import org.junit.Ignore;
import org.junit.Test;
import play.test.TestBrowser;

import java.util.function.Consumer;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.*;

public abstract class AbstractIntegrationTest {
    public static final String FRANZ = "Franz";

    public static void as(String username, final Consumer<TestBrowser> block){
        running(testServer(3333, fakeApplication()), FIREFOX, browser -> {
            browser.goTo("http://localhost:3333/");
            browser.fill("#email").with(username);
            browser.submit("#btn_login");
            assertNotEquals("Login failed!", "http://localhost:3333/login", browser.url());
            block.accept(browser);
        });

    }

}
