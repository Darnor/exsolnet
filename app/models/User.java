package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Formula;
import models.builders.UserBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "exoluser")
public class User extends Model {

    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = COLUMN_USERNAME, unique = true)
    @Basic
    @NotNull
    private String username;

    @Column(name = COLUMN_EMAIL, unique = true)
    @NotNull
    private String email;

    // Currently not needed, must be in the final version.
    //@NotNull
    private String password;

    @Formula(select = "(select coalesce(sum(value),0) from exoluser u left outer join exercise on u.id = exercise.user_id left outer join solution on exercise.id = solution.exercise_id LEFT JOIN vote ON (exercise.id = vote.exercise_id OR solution.id = vote.solution_id) where u.id = ${ta}.id group by u.id)")
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

    public void setId(Long id) {
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

    public void addSolution(Solution solution) {
        solutions.add(solution);
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

    public List<Tracking> getTrackings() {
        return Collections.unmodifiableList(trackings);
    }

    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPoints() {
        return points;
    }

    public List<Tag> getTrackedTags() {
        return trackings.stream().map(Tracking::getTag).collect(Collectors.toList());
    }

    public long getNofCompletedExercisesByTag(Tag tag) {
        return solutions.stream().filter(s -> s.getExercise().getTags().contains(tag)).count();
    }

    /**
     * Authenticates the user depending on email/Username and password combination.
     * Password is ignored for now, should in later stages be hashed and compared with value in database
     * <p>
     * CARE: password not used in this implementation
     *
     * @param userLogin Mail address or Username of User, who wants to login
     * @param password  Password of User, who wants to login
     * @return user
     */
    public static User authenticate(String userLogin, String password) {
        User user = userLogin.contains("@") ? findUserByEmail(userLogin) : findUserByUsername(userLogin);
        /*if (user == null) {
            //create user if non existing
            if (userLogin.contains("@")) {
                user = UserBuilder.anUser().withEmail(userLogin).withUsername(userLogin.split("@")[0]).build();
            } else {
                user = UserBuilder.anUser().withUsername(userLogin).withEmail(userLogin + "@hsr.ch").build();
            }
            user.save();
        }*/
        return user;
    }

    /**
     * Queries database, and returns User holding given email address
     *
     * @param email
     * @return User
     */
    public static User findUserByEmail(String email) {
        return find().where().ieq(COLUMN_EMAIL, email).findUnique();
    }

    /**
     * Queries database, and returns User holding given username
     *
     * @param username
     * @return User
     */
    public static User findUserByUsername(String username) {
        return find().where().ieq(COLUMN_USERNAME, username).findUnique();
    }

    /**
     * Checks if the User has solved the exercise
     *
     * @param id of the exercise
     * @return boolean
     */
    public boolean hasSolved(long id) {
        return solutions.stream().filter(s -> s.getExercise().getId().equals(id)).findFirst().orElse(null) != null;
    }

    public static User create(String username, String email, String password, boolean isModerator) {
        User user = UserBuilder.anUser().withUsername(username).withEmail(email).withPassword(password).withIsModerator(isModerator).build();
        user.save();
        return user;
    }

    public static void update(Long id, String username, String email, String password, boolean isModerator) {
        User user = find().byId(id);
        if (user == null) {
            throw new IllegalArgumentException("Not a valid user");
        }
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setIsModerator(isModerator);
        user.update();
    }

    /**
     * Return Finder Object for DB's queries.
     * <p>
     * CARE: should not be used! may be removed on next version
     *
     * @return Finder for User Models
     */
    public static Model.Finder<Long, User> find() {
        return new Finder<>(User.class);
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }
}
