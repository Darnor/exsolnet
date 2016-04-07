package models.builders;

import models.Exercise;
import models.Solution;
import models.User;
import models.Vote;

/**
 * Created by tourn on 7.4.16.
 */
public class VoteBuilder {
    private Long id;
    private int value;
    private Solution solution;
    private Exercise exercise;
    private User user;

    private VoteBuilder() {
    }

    public static VoteBuilder aVote() {
        return new VoteBuilder();
    }

    public VoteBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public VoteBuilder withValue(int value) {
        this.value = value;
        return this;
    }

    public VoteBuilder withSolution(Solution solution) {
        this.solution = solution;
        return this;
    }

    public VoteBuilder withExercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public VoteBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public VoteBuilder but() {
        return aVote().withId(id).withValue(value).withSolution(solution).withExercise(exercise).withUser(user);
    }

    public Vote build() {
        Vote vote = new Vote();
        vote.setId(id);
        vote.setValue(value);
        vote.setSolution(solution);
        vote.setExercise(exercise);
        vote.setUser(user);
        return vote;
    }
}
