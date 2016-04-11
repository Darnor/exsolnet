package models.builders;

import models.Report;
import models.User;

/**
 * Created by tourn on 11.4.16.
 */
public class ReportBuilder {
    private Long id;
    private String message;
    private User user;

    private ReportBuilder() {
    }

    public static ReportBuilder aReport() {
        return new ReportBuilder();
    }

    public ReportBuilder withId(Long id) {
        this.id = id;
        return this;
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
        return aReport().withId(id).withMessage(message).withUser(user);
    }

    public Report build() {
        Report report = new Report();
        report.setId(id);
        report.setMessage(message);
        report.setUser(user);
        return report;
    }
}
