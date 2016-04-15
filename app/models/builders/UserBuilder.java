package models.builders;

import models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class UserBuilder {
    private Long id;
    private String email;
    private String password;
    private long points;
    private List<Exercise> exercises;
    private List<Solution> solutions;
    private List<Report> reports;
    private List<Comment> comments;
    private List<Vote> votes;
    private List<Tracking> trackings;

    private UserBuilder() {
        exercises = new ArrayList<>();
        solutions = new ArrayList<>();
        reports = new ArrayList<>();
        comments = new ArrayList<>();
        votes = new ArrayList<>();
        trackings = new ArrayList<>();
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

    public UserBuilder withPoints(long points) {
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
        return anUser().withEmail(email).withPassword(password).withPoints(points).withExercises(exercises).withSolutions(solutions).withReports(reports).withComments(comments).withVotes(votes).withTrackings(trackings);
    }

    public User build() {
        User user = new User(email, password);
        user.setId(id);
        user.incrementPointsBy(points);
        for (Exercise exercise : exercises) {
            user.addExercise(exercise);
        }
        for (Solution solution : solutions) {
            user.addSolution(solution);
        }
        for (Report report : reports) {
            user.addReport(report);
        }
        for (Vote vote : votes) {
            user.addVote(vote);
        }
        user.setTrackings(trackings);
        return user;
    }
}
