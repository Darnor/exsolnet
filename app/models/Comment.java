package models;

import com.avaje.ebean.Model;
import scala.annotation.meta.setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="comment")
public class Comment extends Model{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "comment")
    private List<Report> reports;

    @ManyToOne
    @JoinColumn(name="solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @Basic
    private Date time = new Date();


    private static final int NOF_RECENT_COMMENTS = 5;


    public String getContent() {
        return content;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void setTime(Date time) {
        this.time = time;
    }


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

    public static class CommentBuilder {
        private Long id;
        private String content;
        private User user;
        private List<Report> reports;
        private Solution solution;
        private Exercise exercise;
        private Date time;

        private CommentBuilder() {
        }

        public static CommentBuilder aComment() {
            return new CommentBuilder();
        }

        public CommentBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CommentBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public CommentBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public CommentBuilder withReports(List<Report> reports) {
            this.reports = reports;
            return this;
        }

        public CommentBuilder withSolution(Solution solution) {
            this.solution = solution;
            return this;
        }

        public CommentBuilder withExercise(Exercise exercise) {
            this.exercise = exercise;
            return this;
        }


        public CommentBuilder withTime(Date time) {
            this.time = time;
            return this;
        }

        public CommentBuilder but() {
            return aComment().withId(id).withContent(content).withUser(user).withReports(reports).withSolution(solution).withExercise(exercise).withTime(time);
        }

        public Comment build() {
            Comment comment = new Comment();
            comment.setId(id);
            comment.setContent(content);
            comment.setUser(user);
            comment.setReports(reports);
            comment.setSolution(solution);
            comment.setExercise(exercise);
            comment.setTime(time);
            return comment;
        }
    }
}
