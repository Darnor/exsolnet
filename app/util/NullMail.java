package util;

import models.User;
import play.Logger;

/**
 * Created by tourn on 31.5.16.
 */
public class NullMail implements Mail {
    @Override
    public void sendVerificationEmail(User user) throws Exception {
        Logger.info("Verification for " + user.getUsername() + " is: http://localhost:9000" + getVerificationPath(user));
    }
}
