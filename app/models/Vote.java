package models;

import com.avaje.ebean.Model;
import models.builders.VoteBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.avaje.ebean.Expr.eq;

@Entity
@Table(name = "vote")
public class Vote extends Model {

    private static final String COLUMN_SOLUTION_ID = "solution_id";
    private static final String COLUMN_EXERCISE_ID = "exercise_id";
    private static final String COLUMN_USER_ID = "user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private int value;

    @ManyToOne
    @JoinColumn(name = COLUMN_SOLUTION_ID)
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = COLUMN_EXERCISE_ID)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = COLUMN_USER_ID)
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

    public static void upvote(User user, Post post) {
        vote(user, 1, post);
    }

    public static void downvote(User user, Post post) {
        vote(user, -1, post);
    }

    private static void vote(User user, int value, Post post) {
        Vote vote = Vote.find().where().eq(COLUMN_USER_ID, user.getId()).or(eq(COLUMN_SOLUTION_ID, post.getId()), eq(COLUMN_EXERCISE_ID, post.getId())).findUnique();
        if (vote == null) {
            vote = VoteBuilder.aVote().withUser(user).withValue(value).build();
            if (post instanceof Exercise) {
                vote.setExercise((Exercise)post);
            } else {
                vote.setSolution((Solution)post);
            }
            vote.save();
        } else if (vote.getValue() != value){
            vote.delete();
        }
    }
}
