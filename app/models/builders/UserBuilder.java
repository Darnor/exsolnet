package models.builders;

import models.*;

import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class UserBuilder {
    private Long id;
    private String email;
    private String password;
    private int points;
    private List<Exercise> exercises;
    private List<Solution> solutions;
    private List<Report> reports;
    private List<Comment> comments;
    private List<Vote> votes;
    private List<Tracking> trackings;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public UserBuilder withExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        return this;
    }

    public UserBuilder withSolutions(List<Solution> solutions) {
        this.solutions = solutions;
        return this;
    }

    public UserBuilder withReports(List<Report> reports) {
        this.reports = reports;
        return this;
    }

    public UserBuilder withComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public UserBuilder withVotes(List<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public UserBuilder withTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
        return this;
    }

    public UserBuilder but() {
        return anUser().withId(id).withEmail(email).withPassword(password).withPoints(points).withExercises(exercises).withSolutions(solutions).withReports(reports).withComments(comments).withVotes(votes).withTrackings(trackings);
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setPoints(points);
        user.setExercises(exercises);
        user.setSolutions(solutions);
        user.setReports(reports);
        user.setComments(comments);
        user.setVotes(votes);
        user.setTrackings(trackings);
        return user;
    }
}
