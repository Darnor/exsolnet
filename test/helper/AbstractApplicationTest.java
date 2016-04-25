package helper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;
import play.test.Helpers;

/**
 * The mother of all tests, cleaning the database _ONCE_ on initialization
 */
public abstract class AbstractApplicationTest {
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
