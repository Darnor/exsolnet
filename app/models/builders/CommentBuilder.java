package models.builders;

import models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tourn on 11.4.16.
 */
public class CommentBuilder {
    private String content;
    private User user;
    private List<Report> reports;
    private Solution solution;
    private Exercise exercise;

    private CommentBuilder() {
        reports = new ArrayList<>();
    }

    public static CommentBuilder aComment() {
        return new CommentBuilder();
    }

    public CommentBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public CommentBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public CommentBuilder withReports(List<Report> reports) {
        this.reports = reports;
        return this;
    }

    public CommentBuilder withSolution(Solution solution) {
        this.solution = solution;
        return this;
    }

    public CommentBuilder withExercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public CommentBuilder but() {
        return aComment().withContent(content).withUser(user).withReports(reports).withSolution(solution).withExercise(exercise);
    }

    public Comment build() {
        Comment comment = new Comment(user);
        comment.setContent(content);
        comment.setSolution(solution);
        comment.setExercise(exercise);
        for (Report report :reports) {
            comment.addReport(report);
        }
        return comment;
    }
}
