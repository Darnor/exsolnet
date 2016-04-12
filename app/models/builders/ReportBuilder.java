package models.builders;

import models.Report;
import models.User;

/**
 * Created by tourn on 11.4.16.
 */
public class ReportBuilder {
    private String message;
    private User user;

    private ReportBuilder() {
    }

    public static ReportBuilder aReport() {
        return new ReportBuilder();
    }

    public ReportBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public ReportBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public ReportBuilder but() {
        return aReport().withMessage(message).withUser(user);
    }

    public Report build() {
        return new Report(user, message);
    }
}
