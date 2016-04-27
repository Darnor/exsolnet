package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Formula;
import models.builders.SolutionBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "solution")
public class Solution extends Post {
    @Basic
    @NotNull
    private boolean isOfficial;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @NotNull
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "solution")
    private List<Report> reports;

    @OneToMany(mappedBy = "solution")
    private List<Comment> comments;

    @OneToMany(mappedBy = "solution")
    private List<Vote> votes;

    @Formula(select = "(select coalesce(sum(value),0) from solution left join vote on vote.solution_id = solution.id where solution.id = ${ta}.id group by solution.id)")
    private long points;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Vote> getVotes() {
        return Collections.unmodifiableList(votes);
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public static Solution create(String content, Exercise exercise, User user) {
        Solution solution = SolutionBuilder.aSolution().withExercise(exercise).withContent(content).withUser(user).build();
        solution.save();
        return solution;
    }

    public static Model.Finder<Long, Solution> find() {
        return new Finder<>(Solution.class);
    }

    /**
     * the data of the solution with the given id
     *
     * @param id the id of the solution
     * @return the solution from the db with the given id, null if it doesnt exist, nullpointer exception if id is null
     */
    public static Solution findById(Long id) {
        return find().where().eq("id", id).findUnique();
    }

    public long getPoints() {
        return points;
    }

}
