package models;

import com.avaje.ebean.Model;
import models.builders.VoteBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "vote")
public class Vote extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private int value;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Model.Finder<Long, Vote> find() {
        return new Finder<>(Vote.class);
    }

    public static void upvote(User user, Solution solution) {
        vote(user, 1, solution);
    }

    public static void downvote(User user, Solution solution) {
        vote(user, -1, solution);
    }

    private static void vote(User user, int value, Solution solution) {
        Vote vote = null;

        vote = Vote.find().where().eq("user_id", user.getId()).eq("solution_id", solution.getId()).findUnique();
        if (vote == null)
            vote = VoteBuilder.aVote().withSolution(solution).withUser(user).withValue(value).build();
        vote.setValue(value);

        vote.save();


    }
}
