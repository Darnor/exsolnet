package models.builders;

import models.*;

import java.util.Date;
import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class CommentBuilder {
    private Long id;
    private String content;
    private User user;
    private List<Report> reports;
    private Solution solution;
    private Exercise exercise;
    private Date time = new Date();

    private CommentBuilder() {
    }

    public static CommentBuilder aComment() {
        return new CommentBuilder();
    }

    public CommentBuilder withId(Long id) {
        this.id = id;
        return this;
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

    public CommentBuilder withTime(Date time) {
        this.time = time;
        return this;
    }

    public CommentBuilder but() {
        return aComment().withId(id).withContent(content).withUser(user).withReports(reports).withSolution(solution).withExercise(exercise).withTime(time);
    }

    public Comment build() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setUser(user);
        comment.setReports(reports);
        comment.setSolution(solution);
        comment.setExercise(exercise);
        comment.setTime(time);
        return comment;
    }
}
