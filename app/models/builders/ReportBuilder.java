package models.builders;

import models.*;

import java.time.LocalDateTime;

public class ReportBuilder {
    private Long id;
    private String message;
    private LocalDateTime time = LocalDateTime.now();
    private User user;
    private Solution solution;
    private Exercise exercise;
    private Comment comment;

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

    public ReportBuilder withTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public ReportBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public ReportBuilder withSolution(Solution solution) {
        this.solution = solution;
        return this;
    }

    public ReportBuilder withExercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public ReportBuilder withComment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public ReportBuilder but() {
        return aReport().withId(id).withMessage(message).withTime(time).withUser(user).withSolution(solution).withExercise(exercise).withComment(comment);
    }

    public Report build() {
        Report report = new Report();
        report.setId(id);
        report.setMessage(message);
        report.setTime(time);
        report.setUser(user);
        report.setSolution(solution);
        report.setExercise(exercise);
        report.setComment(comment);
        return report;
    }
}
