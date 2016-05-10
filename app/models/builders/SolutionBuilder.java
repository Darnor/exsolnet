package models.builders;

import models.*;

import java.time.LocalDateTime;
import java.util.List;

public class SolutionBuilder {
    private boolean isOfficial;
    private Exercise exercise;
    private User user;
    private List<Comment> comments;
    private List<Vote> votes;
    private Long id;
    private String content;
    private LocalDateTime time = LocalDateTime.now();

    private SolutionBuilder() {
    }

    public static SolutionBuilder aSolution() {
        return new SolutionBuilder();
    }

    public SolutionBuilder withIsOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
        return this;
    }

    public SolutionBuilder withExercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public SolutionBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public SolutionBuilder withComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public SolutionBuilder withVotes(List<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public SolutionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public SolutionBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public SolutionBuilder withTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public SolutionBuilder but() {
        return aSolution().withIsOfficial(isOfficial).withExercise(exercise).withUser(user).withComments(comments).withVotes(votes).withId(id).withContent(content).withTime(time);
    }

    public Solution build() {
        Solution solution = new Solution();
        solution.setIsOfficial(isOfficial);
        solution.setExercise(exercise);
        solution.setUser(user);
        solution.setComments(comments);
        solution.setVotes(votes);
        solution.setId(id);
        solution.setContent(content);
        solution.setTime(time);
        return solution;
    }
}
