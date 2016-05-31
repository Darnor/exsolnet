package util;

import com.google.inject.Inject;
import models.User;
import org.apache.commons.mail.SimpleEmail;
import play.Configuration;

import javax.mail.internet.InternetAddress;
import java.util.Arrays;

/**
 * Created by tourn on 30.5.16.
 */
public class SMTPMail implements Mail{
    @Inject
    Configuration configuration;

    private static final String MESSAGE = "Hallo %s!\n\nDu erhältst diese Mail, weil du dich bei Exsolnet registriert hast. Um dein Konto zu aktivieren, klicke auf diesen Link oder kopiere ihn in die Adressleiste deines Browsers: \n%s\n\nBis bald,\nDein Exsolnet Team";

    public void sendVerificationEmail(User user) throws Exception{
        if(user.getVerificationCode() == null){
            throw new IllegalArgumentException("User is already verified");
        }
        SimpleEmail mail = new SimpleEmail();
        mail.setHostName("localhost");
        mail.setFrom("noreply@exsolnet.trq.ch");
        mail.setTo(Arrays.asList(new InternetAddress[]{ new InternetAddress(user.getEmail())}));
        mail.setSubject("Exsolnet Verifizierung");
        mail.setMsg(String.format(MESSAGE, user.getUsername(), verificationUrl(user)));
        mail.send();
    }

    private String verificationUrl(User user){
        //FIXME remove default
        return configuration.getString("exsolnet.domain", "https://exsolnet.trq.ch") + getVerificationPath(user);
    }
}
