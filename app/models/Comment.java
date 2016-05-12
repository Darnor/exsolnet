package models;

import com.avaje.ebean.Model;
import models.builders.CommentBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name="comment")
public class Comment extends Model {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Lob
    @NotNull
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name="solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time = LocalDateTime.now();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastchanged;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public LocalDateTime getLastChanged() {
        return lastchanged;
    }

    public void setLastChanged(LocalDateTime lastchanged) {
        this.lastchanged = lastchanged;
    }

    public static Model.Finder<Long, Comment> find(){
        return new Model.Finder<>(Comment.class);
    }

    /**
     * The Comment with the given id
     * @param id the id of the comment
     * @return the comment or null if not found.
     */
    public static Comment findById(Long id) {
        return find().byId(id);
    }

    /**
     * Create new comment for a Exercise
     * @param content the comment
     * @param exercise the associated exercise
     * @param user the user who created the comment
     * @return the created commit
     */
    public static Comment create(String content, Exercise exercise, User user) {
        Comment comment = CommentBuilder.aComment().withExercise(exercise).withContent(content).withUser(user).build();
        comment.save();
        return comment;
    }

    /**
     * Create a new comment for a Solution
     * @param content the comment
     * @param solution the associated solution
     * @param user the user who created the comment
     * @return the created comment
     */
    public static Comment create(String content, Solution solution, User user) {
        Comment comment = CommentBuilder.aComment().withSolution(solution).withContent(content).withUser(user).build();
        comment.save();
        return comment;
    }

    /**
     * Update a comment with a new content
     * @param commentId the id of the comment
     * @param content the new content of the comment
     * @return the updated comment
     */
    public static Comment updateContent(Long commentId, String content){
        Comment comment = findById(commentId);
        comment.setContent(content);
        comment.setLastChanged(LocalDateTime.now());
        comment.update();
        return comment;
    }
}
