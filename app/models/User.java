package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static models.builders.UserBuilder.anUser;

/**
 * Created by mario on 21.03.16.
 */
@Entity
@Table(name="exoluser")
public class User extends Model {

    private static final String COLUMN_EMAIL = "email";

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = COLUMN_EMAIL, unique=true)
    @NotNull
    private String email;

    // Currently not needed, must be in the final version.
    //@NotNull
    private String password;

    @NotNull
    private long points;

    @NotNull
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

    @OneToMany
    @JoinColumn(name = Tracking.COLUMN_USER_ID)
    private List<Tracking> trackings;

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setIsModerator(boolean moderator) {
        isModerator = moderator;
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Solution> getSolutions() {
        return Collections.unmodifiableList(solutions);
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public List<Vote> getVotes() {
        return Collections.unmodifiableList(votes);
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }

    public List<Tracking> getTrackings() {
        return trackings;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public List<Tag> getTrackedTags() {
        return trackings.stream().map(Tracking::getTag).collect(Collectors.toList());
    }

    public long getNofCompletedExercisesByTag(Tag tag) {
        return solutions.stream().filter(s -> s.getExercise().getTags().contains(tag)).count();
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
        return find().where().ieq(COLUMN_EMAIL, email).findUnique();
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
}
