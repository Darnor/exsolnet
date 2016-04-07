package models.builders;

import models.*;

import java.sql.Date;
import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class ExerciseBuilder {
    private String title;
    private List<Solution> solutions;
    private List<Vote> votes;
    private List<Tag> tags;
    private User user;
    private List<Report> reports;
    private List<Comment> comments;
    private Long id;
    private String content;
    private Date time;
    private int points;

    private ExerciseBuilder() {
    }

    public static ExerciseBuilder anExercise() {
        return new ExerciseBuilder();
    }

    public ExerciseBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ExerciseBuilder withSolutions(List<Solution> solutions) {
        this.solutions = solutions;
        return this;
    }

    public ExerciseBuilder withVotes(List<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public ExerciseBuilder withTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public ExerciseBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public ExerciseBuilder withReports(List<Report> reports) {
        this.reports = reports;
        return this;
    }

    public ExerciseBuilder withComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public ExerciseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ExerciseBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public ExerciseBuilder withTime(Date time) {
        this.time = time;
        return this;
    }

    public ExerciseBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public ExerciseBuilder but() {
        return anExercise().withTitle(title).withSolutions(solutions).withVotes(votes).withTags(tags).withUser(user).withReports(reports).withComments(comments).withId(id).withContent(content).withTime(time).withPoints(points);
    }

    public Exercise build() {
        Exercise exercise = new Exercise();
        exercise.setTitle(title);
        exercise.setSolutions(solutions);
        exercise.setVotes(votes);
        exercise.setTags(tags);
        exercise.setUser(user);
        exercise.setReports(reports);
        exercise.setComments(comments);
        exercise.setId(id);
        exercise.setContent(content);
        exercise.setTime(time);
        exercise.setPoints(points);
        return exercise;
    }
}
