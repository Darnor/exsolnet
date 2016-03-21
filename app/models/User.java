package models;

import play.data.validation.Constraints;

/**
 * Created by mario on 21.03.16.
 */
public class User {

    @Constraints.Required
    protected String email;

    protected String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
