package models.builders;

import models.Exercise;
import models.Solution;
import models.User;

import java.time.LocalDateTime;

/**
 * Created by tourn on 11.4.16.
 */
public class SolutionBuilder {
    private User user;
    private Exercise exercise;
    private String content;
    private LocalDateTime time = LocalDateTime.now();
    private int points;

    private SolutionBuilder() {
    }

    public static SolutionBuilder aSolution() {
        return new SolutionBuilder();
    }

    public SolutionBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public SolutionBuilder withExercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public SolutionBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public SolutionBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public SolutionBuilder but() {
        return aSolution().withUser(user).withContent(content).withPoints(points);
    }

    public Solution build() {
        Solution solution = new Solution(user, exercise);
        solution.setContent(content);
        return solution;
    }
}
