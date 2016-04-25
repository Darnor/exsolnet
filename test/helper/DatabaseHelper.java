package helper;

import com.avaje.ebean.Ebean;
import org.apache.commons.io.FileUtils;
import play.Application;

/**
 * Created by tourn on 25.4.16.
 */
public class DatabaseHelper {
    public static void cleanDB(Application app) {
        try {
            System.out.println("Cleaning DB");
            String clean = FileUtils.readFileToString(app.getWrappedApplication().getFile("test/clean.sql"));
            Ebean.execute(Ebean.createCallableSql(clean));

            System.out.println("Inserting default test data");
            String testData = FileUtils.readFileToString(app.getWrappedApplication().getFile("test/data.sql"));
            Ebean.execute(Ebean.createCallableSql(testData));
        } catch (Exception e){
            throw new RuntimeException("Problem cleaning database: ", e);
        }
    }
}
