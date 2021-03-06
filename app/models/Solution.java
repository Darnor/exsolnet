package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Formula;
import models.builders.SolutionBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private List<Comment> comments;

    @OneToMany(mappedBy = "solution")
    private List<Vote> votes;

    @Formula(select = "(SELECT coalesce(sum(value),0) FROM vote v INNER JOIN solution s ON v.solution_id = s.id WHERE s.id = ${ta}.id)")
    private long points;

    @Column(columnDefinition = "boolean NOT NULL DEFAULT TRUE")
    private boolean valid = true;

    public static Model.Finder<Long, Solution> find() {
        return new Finder<>(Solution.class);
    }

    public static Solution create(String content, Exercise exercise, User user, Boolean isOfficial) {
        Solution solution = SolutionBuilder.aSolution().withExercise(exercise).withContent(content).withUser(user).withIsOfficial(isOfficial).build();
        solution.save();
        return solution;
    }

    public static Solution create(String content, Exercise exercise, User user) {
        return create(content, exercise, user, false);
    }

    public static Solution update(long id, String content) {
        return update(id, content, false);
    }

    public static Solution update(long id, String content, Boolean isOfficial) {
        Solution solution = Solution.findById(id);
        solution.setContent(content);
        solution.setLastChanged(LocalDateTime.now());
        solution.setIsOfficial(isOfficial);
        solution.update();
        return solution;
    }

    /**
     * the data of the solution with the given id
     *
     * @param id the id of the solution
     * @return the solution from the db with the given id, null if it doesnt exist, nullpointer exception if id is null
     */
    public static Solution findValidById(long id) {
        Solution solution = find().byId(id);
        return (solution == null || !solution.isValid()) ? null : solution;
    }

    public static Solution findById(Long id) {
        return find().byId(id);
    }

    /**
     * deletes Solution
     *
     * @param id solutionId to delete
     */
    public static void delete(long id) {
        Solution solution = findValidById(id);
        solution.setValid(false);
        solution.save();
    }

    /**
     * undo deletion of Solution
     *
     * @param id solutionId to delete
     */
    public static void undoDelete(long id) {
        Solution solution = findById(id);
        solution.setValid(true);
        solution.save();
    }

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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public List<Comment> getComments() {
        comments.removeIf(comment -> !comment.isValid());
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

    public long getPoints() {
        return points;
    }

    public int getUserVoteValue(long userId) {
        return getVotes().stream()
                .parallel()
                .filter(v -> v.getUser().getId().equals(userId))
                .map(Vote::getValue)
                .findFirst().orElse(0);
    }
}
