package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name = "vote")
public class Vote extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int value;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Vote(User user, Solution solution, Exercise exercise) {
        this.id = null;
        this.user = user;
        this.solution = solution;
        this.exercise = exercise;
    }

    public void downVote() {
        value = (value == 1) ? 0 : -1;
    }

    public void upVote() {
        value = (value == -1) ? 0 : 1;
    }

    public Long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Solution getSolution() {
        return solution;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public User getUser() {
        return user;
    }

}
