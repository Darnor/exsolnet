package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static models.builders.UserBuilder.anUser;

/**
 * Created by mario on 21.03.16.
 */
@Entity
@Table(name="exoluser")
public class User extends Model{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    @NotNull
    private String email;

    private String password;

    private long points;
    private boolean isModerator;

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

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.points = 0;
        this.isModerator = false;
        this.exercises = new ArrayList<>();
        this.solutions = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.trackings = new ArrayList<>();
    }

    /**
      * Gets the Exercises of the User.
      *
      * @return Exercises of the User.
      */
    public List<Exercise> getExercises() {
        return exercises;
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
      * Gets the Votes of the User.
      *
      * @return the Votes of the User.
      */
    public List<Vote> getVotes() {
        return votes;
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
      * Sets the Email of the User.
      *
      * @param email of the User.
      */
    public void setEmail(String email) {
        if (email == null || email.length() == 0) {
            throw new IllegalArgumentException("Email cannot be null and must be at east one character long.");
        }
        this.email = email;
    }

    /**
      * Gets the Points of the User.
      *
      * @return the Points of the User.
      */
    public long getPoints() {
        return points;
    }

    /**
     * In- or decrement points
     *
     * @param amount Points to be added (or subtracted if its negative)
     */
    public void incrementPointsBy(long amount) {
        points += amount;
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
      * @param moderator of the User.
      */
    public void setModerator(boolean moderator) {
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
      * Gets the Comments of the User.
      *
      * @return the Comments of the User.
      */
    public List<Comment> getComments() {
        return comments;
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
      * @param password of the User.
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
        return getTrackings().stream().map(Tracking::getTag).collect(Collectors.toList());
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
        return find().where().ieq("email", email).findUnique();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
