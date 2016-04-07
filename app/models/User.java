package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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
      * @return Exercises of the User.
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

    /**
      * Sets the Solutions of the User.
      * 
      * @param Solutions of the User.
      */
    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    /**
      * Gets the Votes of the User.
      *
      * @return the Votes of the User.
      */
    public List<Vote> getVotes() {
        return votes;
    }

    /**
      * Sets the Votes of the User.
      * 
      * @param Votes of the User.
      */
    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    /**
      * Gets the Reports of the User.
      *
      * @return the Reports of the User.
      */
    public List<Report> getReports() {
        return reports;
    }

    /**
      * Sets the Reports of the User.
      * 
      * @param Reports of the User.
      */
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    /**
      * Sets the Email of the User.
      * 
      * @param Email of the User.
      */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
      * Sets the Id of the User.
      * 
      * @param Id of the User.
      */
    public void setId(Long id) {
        this.id = id;
    }

    /**
      * Gets the Points of the User.
      *
      * @return the Points of the User.
      */
    public int getPoints() {
        return points;
    }

    /**
      * Sets the Points of the User.
      * 
      * @param Points of the User.
      */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
      * Gets the ModeratorBoolean of the User.
      *
      * @return the ModeratorBoolean of the User.
      */
    public Boolean getModerator() {
        return isModerator;
    }

    /**
      * Sets the ModeratorBoolean of the User.
      * 
      * @param ModeratorBoolean of the User.
      */
    public void setModerator(Boolean moderator) {
        isModerator = moderator;
    }

    /**
      * Gets the Trackings of the User.
      *
      * @return the Trackings of the User.
      */
    public List<Tracking> getTrackings() {
        return trackings;
    }

    /**
      * Sets the Trackings of the User.
      * 
      * @param Trackings of the User.
      */
    
    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }

    /**
      * Gets the Comments of the User.
      *
      * @return the Comments of the User.
      */
    public List<Comment> getComments() {
        return comments;
    }

    /**
      * Sets the Comments of the User.
      * 
      * @param Comments of the User.
      */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
      * Gets the Email of the User.
      *
      * @return the Email of the User.
      */
    public String getEmail() {
        return email;
    }

    /**
      * Sets the Password of the User.
      * 
      * @param Password of the User.
      */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
      * Gets the Password of the User.
      *
      * @return the Password of the User.
      */
    public String getPassword() {
        return password;
    }

    /**
      * Gets the Id of the User.
      *
      * @return the Id of the User.
      */
    public Long getId() {
        return id;
    }

    public List<Tag> getTrackedTags() {
        return getTrackings().stream().map(tracking -> tracking.getTag()).collect(Collectors.toList());
    }

    /**
     * Authenticates the user depending on email and password combination.
     * Password is ignored for now, should in later stages be hashed and compared with value in database
     * <p>
     * CARE: password not used in this implementation
     *
     * @param email    Mail address of User, who wants to login
     * @param password Password of User, who wants to login
     * @return user
     */
    public static User authenticate(String email, String password) {
        User user = findUser(email);
        if (user == null) {
            //create user if non existing
            user = anUser().withEmail(email).build();
            user.save();
        }
        return user;
    }

    /**
     * Queries database, and returns User holding given email address
     * @param email
     * @return User
     */
    public static User findUser(String email) {
        return find().where().eq("email", email).findUnique();
    }

    /**
     * Return Finder Object for DB's queries.
     *
     * CARE: should not be used! may be removed on next version
     *
     * @return Finder for User Models
     */
    public static Model.Finder<Long, User> find() {
        return new Finder<>(User.class);
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
