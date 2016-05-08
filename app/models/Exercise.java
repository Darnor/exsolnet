package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.Query;
import com.avaje.ebean.annotation.Formula;
import models.builders.ExerciseBuilder;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "exercise")
public class Exercise extends Post {

    @Constraints.Required
    @NotNull
    private String title;

    @Formula(select = "(SELECT count(*) FROM solution _s WHERE _s.exercise_id = ${ta}.id)")
    private int solutionCount;

    @Formula(select = "(SELECT coalesce(sum(value),0) FROM vote v INNER JOIN exercise e ON v.exercise_id = e.id WHERE e.id = ${ta}.id)")
    private long points;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Solution> solutions;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Vote> votes;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "exercise_tag",
            joinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(columnDefinition = "boolean NOT NULL DEFAULT FALSE")
    private boolean deleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public long getPoints() {
        return points;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private void fillData(String title, String content, List<Tag> tags, User user) {
        this.setTitle(title);
        this.setContent(content);
        this.setUser(user);
        this.setTags(tags);
    }

    public static Exercise create(String title, String content, List<Tag> tags, User user) {
        Exercise exercise = ExerciseBuilder.anExercise().build();
        exercise.fillData(title, content, tags, user);
        exercise.save();
        return exercise;
    }

    public static Exercise update(long id, String title, String content, List<Tag> tags, User user) {
        Exercise exercise = findById(id);
        if (exercise == null) {
            throw new IllegalArgumentException("Invalid exercise id");
        }
        exercise.fillData(title, content, tags, user);
        exercise.update();
        return exercise;
    }

    /**
     * Delete exercise cascading
     * @param id exerciseId to delete
     */
    public static void delete(long id){
        Exercise exercise = findById(id);
        exercise.setDeleted(true);
        exercise.save();
    }

    public static Model.Finder<Long, Exercise> find() {
        return new Finder<>(Exercise.class);
    }

    /**
     * Returns a Paged List with the given filters and order
     *
     * @param pageNr      the page number
     * @param orderBy     the sorted attributname
     * @param titleFilter the string which is used for filter/query the title
     * @param tagFilter   the string-array which contains the tags for filtering
     * @param pageSize    the count of exercises for one page
     * @return the PagedList for the actual page and filters/orders
     */
    public static PagedList<Exercise> getPagedList(int pageNr, String orderBy, String titleFilter, String[] tagFilter, int pageSize) {
        Query<Exercise> query = Ebean.createQuery(Exercise.class);
        query.where().icontains("title", titleFilter);
        if (!"".equals(tagFilter[0])) {
            query.where().in("tags.name", Arrays.asList(tagFilter));
        }
        query.where().eq("deleted", false);
        return query.orderBy(orderBy).findPagedList(pageNr, pageSize);
    }

    /**
     * the data of the exercise with the given id
     *
     * @param id the id of the exercise
     * @return the exercise from the db with the given id, null if it doesnt exist, nullpointer exception if id is null
     */
    public static Exercise findById(Long id) {
        Exercise exercise = find().byId(id);
        if(exercise == null){
            return null;
        }else {
            return exercise.isDeleted() ? null : exercise;
        }
    }

    /**
     * Get all Tags sorted by isMainTag or if equal by alphabet.
     *
     * @return returns the sorted Tag list
     */
    public List<Tag> getTagsSortedByTagType() {
        return tags.stream().sorted((t1, t2) -> {
            if (t1.isMainTag() == t2.isMainTag()) {
                return t1.getName().compareToIgnoreCase(t2.getName());
            }
            return t1.isMainTag() ? -1 : 1;
        }).collect(Collectors.toList());
    }

}
