package controllers;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static play.test.Helpers.*;

public class GlobalControllerTest {
    @Test
    public void testUnhandledRoutesRedirectedToPageNotFound() {
        running(testServer(3333, fakeApplication()), FIREFOX, browser -> {
            browser.goTo("http://localhost:3333/DefinitlyNonExistingRoutROFLMAO");
            assertThat(browser.pageSource(), containsString("Seite nicht gefunden"));
        });
    }



}
