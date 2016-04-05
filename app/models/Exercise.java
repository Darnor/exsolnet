package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by mario on 21.03.16.
 */

@Entity
@Table(name = "exercise")
public class Exercise extends Post {

    @Constraints.Required
    private String title;

    @OneToMany(mappedBy = "exercise")
    private List<Solution> solutions;


    @OneToMany(mappedBy = "exercise")
    private List<Vote> votes;


    @ManyToMany
    @JoinTable(
            name = "exercise_tag",
            joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "exercise")
    private List<Report> reports;

    @OneToMany(mappedBy = "exercise")
    private List<Comment> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static class ExerciseBuilder {
        private String title;
        private List<Solution> solutions;
        private List<Vote> votes;
        private List<Tag> tags;
        private User user;
        private List<Report> reports;
        private List<Comment> comments;
        private Long id;
        private String content;
        private Date time;
        private int points;

        private ExerciseBuilder() {
        }

        public static ExerciseBuilder anExercise() {
            return new ExerciseBuilder();
        }

        public ExerciseBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public ExerciseBuilder withSolutions(List<Solution> solutions) {
            this.solutions = solutions;
            return this;
        }

        public ExerciseBuilder withVotes(List<Vote> votes) {
            this.votes = votes;
            return this;
        }

        public ExerciseBuilder withTags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public ExerciseBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public ExerciseBuilder withReports(List<Report> reports) {
            this.reports = reports;
            return this;
        }

        public ExerciseBuilder withComments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public ExerciseBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ExerciseBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public ExerciseBuilder withTime(Date time) {
            this.time = time;
            return this;
        }

        public ExerciseBuilder withPoints(int points) {
            this.points = points;
            return this;
        }

        public ExerciseBuilder but() {
            return anExercise().withTitle(title).withSolutions(solutions).withVotes(votes).withTags(tags).withUser(user).withReports(reports).withComments(comments).withId(id).withContent(content).withTime(time).withPoints(points);
        }

        public Exercise build() {
            Exercise exercise = new Exercise();
            exercise.setTitle(title);
            exercise.setSolutions(solutions);
            exercise.setVotes(votes);
            exercise.setTags(tags);
            exercise.setUser(user);
            exercise.setReports(reports);
            exercise.setComments(comments);
            exercise.setId(id);
            exercise.setContent(content);
            exercise.setTime(time);
            exercise.setPoints(points);
            return exercise;
        }
    }
}
