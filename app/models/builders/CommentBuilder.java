package models.builders;

import models.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by tourn on 11.4.16.
 */
public class CommentBuilder {
    private long id;
    private String content;
    private User user;
    private List<Report> reports;
    private Solution solution;
    private Exercise exercise;
    private LocalDateTime time = LocalDateTime.now();

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

    public CommentBuilder withTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public CommentBuilder but() {
        return aComment().withId(id).withContent(content).withUser(user).withReports(reports).withSolution(solution).withExercise(exercise).withTime(time);
    }

    public Comment build() {
        Comment comment = new Comment(user);
        comment.setContent(content);
        comment.setSolution(solution);
        comment.setExercise(exercise);
        return comment;
    }
}
