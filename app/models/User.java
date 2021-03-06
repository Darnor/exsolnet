package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Formula;
import models.builders.UserBuilder;
import util.MD5Util;
import util.SecurityUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static models.Tracking.COLUMN_USER_ID;

@Entity
@Table(name = "exoluser")
public class User extends Model {

    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final int NOF_RECENT_COMMENTS = 5;

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

    @NotNull
    private String password;

    @Formula(select = "(SELECT coalesce(sum(value),0) FROM (SELECT value FROM vote v INNER JOIN exercise e ON e.id = v.exercise_id WHERE e.user_id = ${ta}.id UNION ALL SELECT value FROM vote v INNER JOIN solution s ON s.id = v.solution_id WHERE s.user_id = ${ta}.id) AS value)")
    private long points;

    @NotNull
    private boolean isModerator;

    @OneToMany(mappedBy = "user")
    private List<Exercise> exercises;

    @OneToMany(mappedBy = "user")
    private List<Solution> solutions;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes;

    @OneToMany
    @JoinColumn(name = COLUMN_USER_ID)
    private List<Tracking> trackings;

    @Basic
    private String verificationCode;


    public static Model.Finder<Long, User> find() {
        return new Finder<>(User.class);
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
        User user = userLogin.contains("@") ? findByEmail(userLogin) : findByUsername(userLogin);
        return user != null && SecurityUtil.checkPassword(password, user.getPassword()) ? user : null;
    }

    /**
     * Queries database, and returns User holding given email address
     *
     * @param email
     * @return User
     */
    public static User findByEmail(String email) {
        return find().where().ieq(COLUMN_EMAIL, email).findUnique();
    }

    /**
     * Queries database, and returns User holding given username
     *
     * @param username
     * @return User
     */
    public static User findByUsername(String username) {
        return find().where().ieq(COLUMN_USERNAME, username).findUnique();
    }

    public static User create(String username, String email, String password, boolean isModerator) throws UnsupportedEncodingException {
        User user = UserBuilder.anUser().withUsername(username).withEmail(email).withPassword(SecurityUtil.hashPassword(password)).withIsModerator(isModerator).build();
        user.setVerificationCode(SecurityUtil.generateVerificationCode());
        user.save();
        return user;
    }

    public static void update(Long id, String username, String password, boolean isModerator) {
        User user = find().byId(id);
        user.setUsername(username);
        if (password != null) {
            user.setPassword(SecurityUtil.hashPassword(password));
        }
        user.setIsModerator(isModerator);
        user.update();
    }

    public static User findById(Long id) {
        return find().byId(id);
    }

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

    public List<Exercise> getValidExercises() {
        exercises.removeIf(exercise -> !exercise.isValid());
        return Collections.unmodifiableList(exercises);
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Solution> getValidSolutions() {
        solutions.removeIf(solution -> !solution.isValid());
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
    
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }


    public List<Tag> getTrackedTags() {
        return trackings.stream().map(Tracking::getTag).collect(Collectors.toList());
    }

    public long getNofCompletedExercisesByTag(Tag tag) {
        return getValidSolutions().stream()
                .filter(s -> s.getExercise().isValid() && s.getExercise().getTags().contains(tag))
                .count();
    }

    /**
     * Checks if the User has solved the exercise
     *
     * @param exerciseId of the exercise
     * @return boolean
     */
    public boolean hasSolved(long exerciseId) {
        return getValidSolutions().stream().parallel().filter(s -> s.getExercise().getId().equals(exerciseId)).findFirst().orElse(null) != null;
    }

    public String getGravatarHash() {
        return MD5Util.md5Hex(getEmail());
    }

    public List<Comment> getRecentComments() {
        Stream<Comment> recentExerciseComments = exercises.stream()
                .parallel()
                .flatMap(e -> e.getComments().stream());

        Stream<Comment> recentSolutionComments = solutions.stream()
                .parallel()
                .flatMap(s -> s.getComments().stream());

        return Stream.concat(recentExerciseComments, recentSolutionComments)
                .sorted((c1, c2) -> c2.getTime().compareTo(c1.getTime()))
                .limit(NOF_RECENT_COMMENTS)
                .collect(Collectors.toList());
    }
}
