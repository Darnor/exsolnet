package repositories;

import com.avaje.ebean.Ebean;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import play.Application;
import play.test.Helpers;

import java.io.IOException;

/**
 * Created by tourn on 6.4.16.
 */
public abstract class AbstractRepositoryTest {
    protected static Application app;

    @BeforeClass
    public static void startUp() {
        app = Helpers.fakeApplication();
        Helpers.start(app);

        cleanDB();
    }

    public static void cleanDB() {
        try {
            System.out.println("Cleaning DB");
            String clean = FileUtils.readFileToString(app.getWrappedApplication().getFile("test/clean.sql"));
            Ebean.execute(Ebean.createCallableSql(clean));

            System.out.println("Inserting default test data");
            String testData = FileUtils.readFileToString(app.getWrappedApplication().getFile("test/data.sql"));
            Ebean.execute(Ebean.createCallableSql(testData));
        } catch (IOException e){
            throw new RuntimeException("Problem cleaning database: ", e);
        }
    }

    @AfterClass
    public static void shutDown(){
        Helpers.stop(app);
    }
}
