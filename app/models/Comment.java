package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="comment")
public class Comment extends Model {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "comment")
    private List<Report> reports;

    @ManyToOne
    @JoinColumn(name="solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time = LocalDateTime.now();

    private static final int NOF_RECENT_COMMENTS = 5;

    public Comment(User user) {
        id = null;
        this.user = user;
        this.reports = new ArrayList<>();
        this.time = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    public void addReport(Report report) {
        reports.add(report);
    }

    public void removeReport(Report report) {
        reports.remove(report);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Solution getSolution() {
        return solution;
    }

    /**
     * @param user
     * @return a list of recent comments that have been added to user's posts
     */
    public static List<Comment> getRecentComments(User user) {
        return find().where()
                .eq("user", user)
                .orderBy("time desc")
                .setMaxRows(NOF_RECENT_COMMENTS)
                .findList();
    }

    public static Model.Finder<Long, Comment> find(){
        return new Model.Finder<>(Comment.class);
    }

}
