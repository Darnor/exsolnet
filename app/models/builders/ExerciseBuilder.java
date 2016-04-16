package models.builders;

import models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tourn on 11.4.16.
 */
public class ExerciseBuilder {
    private String title;
    private List<Solution> solutions;
    private List<Vote> votes;
    private List<Tag> tags;
    private User user;
    private List<Report> reports;
    private List<Comment> comments;
    private String content;
    private long points;
    private Long id;

    private ExerciseBuilder() {
        solutions = new ArrayList<>();
        votes = new ArrayList<>();
        tags = new ArrayList<>();
        reports = new ArrayList<>();
        comments = new ArrayList<>();
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

    public ExerciseBuilder withId(Long id) {
        this.id = id;
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

    public ExerciseBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public ExerciseBuilder withPoints(long points) {
        this.points = points;
        return this;
    }


    public ExerciseBuilder but() {
        return anExercise().withTitle(title).withSolutions(solutions).withVotes(votes).withTags(tags).withUser(user).withReports(reports).withComments(comments).withContent(content).withPoints(points);
    }

    public Exercise build() {
        Exercise exercise = new Exercise();
        exercise.setUser(user);
        exercise.setTitle(title);
        exercise.setContent(content);
        exercise.setPoints(points);
        exercise.setId(id);
        exercise.setSolutions(solutions);
        exercise.setVotes(votes);
        exercise.setTags(tags);
        exercise.setComments(comments);
        exercise.setReports(reports);
        return exercise;
    }


}
