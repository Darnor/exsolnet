import com.google.inject.AbstractModule;
import play.Configuration;
import play.Environment;
import util.Mail;
import util.NullMail;
import util.SMTPMail;

/**
 * Created by tourn on 31.5.16.
 */
public class Module extends AbstractModule{
    private final Environment environment;
    private final Configuration configuration;

    public Module(
            Environment environment,
            Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        switch(environment.mode()){
            case PROD:
                bind(Mail.class).to(SMTPMail.class);
                break;
            default:
                bind(Mail.class).to(NullMail.class);
                break;
        }
    }
}
