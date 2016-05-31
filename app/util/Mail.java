package util;

import com.google.inject.ImplementedBy;
import models.User;

/**
 * Created by tourn on 31.5.16.
 */
@ImplementedBy(SMTPMail.class)
public interface Mail {
    public void sendVerificationEmail(User user) throws Exception;
    default String getVerificationPath(User user){
        if(user == null) { throw new IllegalArgumentException("User cannot be null"); }
        return "/verify/" + user.getId() + "/" + user.getVerificationCode();
    }
}
