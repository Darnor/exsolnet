package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mario on 21.03.16.
 */
@Entity
@Table(name="exoluser")
public class User extends Model{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private int points;
    private Boolean isModerator;

    @OneToMany(mappedBy = "user")
    private List<Exercise> exercises;

    @OneToMany(mappedBy = "user")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "user")
    private List<Report> reports;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes;

    @OneToMany(mappedBy = "user")
    private List<Tracking> trackings;

    /**
      * Gets the Exercises of the User.
      *
      * @return List<Exercise> the Exercises of the User.
      */
    public List<Exercise> getExercises() {
        return exercises;
    }

    /**
      * Sets the Exercises of the User.
      * 
      * @param Exercises of the User.
      */
    
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    /**
      * Gets the Solutions of the User.
      *
      * @return the Solutions of the User.
      */
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

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Boolean getModerator() {
        return isModerator;
    }

    public void setModerator(Boolean moderator) {
        isModerator = moderator;
    }


    public List<Tracking> getTrackings() {
        return trackings;
    }

    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public static class UserBuilder {
        private Long id;
        private String email;
        private String password;
        private int points;
        private List<Exercise> exercises;
        private List<Solution> solutions;
        private List<Report> reports;
        private List<Comment> comments;
        private List<Vote> votes;
        private List<Tracking> trackings;

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withPoints(int points) {
            this.points = points;
            return this;
        }

        public UserBuilder withExercises(List<Exercise> exercises) {
            this.exercises = exercises;
            return this;
        }

        public UserBuilder withSolutions(List<Solution> solutions) {
            this.solutions = solutions;
            return this;
        }

        public UserBuilder withReports(List<Report> reports) {
            this.reports = reports;
            return this;
        }

        public UserBuilder withComments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public UserBuilder withVotes(List<Vote> votes) {
            this.votes = votes;
            return this;
        }

        public UserBuilder withTrackings(List<Tracking> trackings) {
            this.trackings = trackings;
            return this;
        }

        public UserBuilder but() {
            return anUser().withId(id).withEmail(email).withPassword(password).withPoints(points).withExercises(exercises).withSolutions(solutions).withReports(reports).withComments(comments).withVotes(votes).withTrackings(trackings);
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setPassword(password);
            user.setPoints(points);
            user.setExercises(exercises);
            user.setSolutions(solutions);
            user.setReports(reports);
            user.setComments(comments);
            user.setVotes(votes);
            user.setTrackings(trackings);
            return user;
        }
    }
}
