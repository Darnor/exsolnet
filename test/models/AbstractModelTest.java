package models;

import helper.DatabaseHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;
import play.test.Helpers;

public abstract class AbstractModelTest {
    protected static Application app;

    @BeforeClass
    public static void startUp() {
        app = Helpers.fakeApplication();
        Helpers.start(app);

        DatabaseHelper.cleanDB(app);
    }

    @AfterClass
    public static void shutDown(){
        Helpers.stop(app);
    }
}
