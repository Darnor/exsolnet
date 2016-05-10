package models.builders;

import models.*;

import java.util.List;

public class UserBuilder {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isModerator;
    private List<Exercise> exercises;
    private List<Solution> solutions;
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

    public UserBuilder withUsername(String username) {
        this.username = username;
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

    public UserBuilder withIsModerator(boolean isModerator) {
        this.isModerator = isModerator;
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
        return anUser().withId(id).withUsername(username).withEmail(email).withPassword(password).withIsModerator(isModerator).withExercises(exercises).withSolutions(solutions).withComments(comments).withVotes(votes).withTrackings(trackings);
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setIsModerator(isModerator);
        user.setExercises(exercises);
        user.setSolutions(solutions);
        user.setComments(comments);
        user.setVotes(votes);
        user.setTrackings(trackings);
        return user;
    }
}
