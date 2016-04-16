package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.Query;
import com.avaje.ebean.annotation.Formula;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by mario on 21.03.16.
 */

@Entity
@Table(name = "exercise")
public class Exercise extends Post {
    
    @Constraints.Required
    @NotNull
    private String title;

    @Formula(select = "(select count(*) from solution _s where _s.exercise_id=${ta}.id)")
    private int solutionCount;

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
    @NotNull
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

    public static void updateOrCreate(Exercise exercise, String title, String content, String[] mainTags, String[] otherTags, User user) {
        if (exercise.getId() != null && !exercise.getId().equals(user.getId()) && !user.getIsModerator()) {
            throw new IllegalArgumentException("User not allowed to edit another users exercise!");
        }
        if (mainTags == null || mainTags.length == 0) {
            throw new IllegalArgumentException("Exercise must contain at least one main tag!");
        }
        List<Tag> tags = Tag.process(mainTags, true);
        if (otherTags != null && otherTags.length > 0) {
            tags.addAll(Tag.process(otherTags, false));
        }
        exercise.setTitle(title);
        exercise.setContent(content);
        exercise.setTags(tags);
        if (exercise.getId() == null) {
            exercise.setUser(user);
            exercise.save();
        } else {
            exercise.update();
        }
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
    //TODO remove from exercise into controller ??
    public static PagedList<Exercise> getPagedList(int pageNr, String orderBy, String titleFilter, String[] tagFilter, int pageSize) {
        Query<Exercise> query = Ebean.createQuery(Exercise.class);
        query.where().contains("title",titleFilter);
        if(!tagFilter[0].equals("")){
            query.where().in("tags.name", Arrays.asList(tagFilter));
        }
        return query.orderBy(orderBy).findPagedList(pageNr, pageSize);
    }

    /**
     * the data of the exercise with the given id
     * @param id the id of the exercise
     * @return the exercise from the db with the fiven id, null if it doesnt exist, nullpointer exception if id is null
     */
    public static Exercise findExerciseData(Long id) {
        return find().where().eq("id", id).findUnique();
    }

    /**
     * Converts the order-Id to the orderBy string
     * @param order the orderID from the HTML-table
     * @return the order-by-attribute-string
     */
    public static String getOrderByAttributeString(int order){
        String result = tableHeaderMap.get(Math.abs(order));
        if(order<0){
            result += " desc";
        }
        return result;
    }

    /**
     * Map the Id of the html exercise-table to their Model-Attribute-name
     */
    private static final Map<Integer, String> tableHeaderMap;
    static
    {
        tableHeaderMap = new HashMap<Integer, String>();
        tableHeaderMap.put(1, "title");
        tableHeaderMap.put(2, "solutionCount");
        tableHeaderMap.put(3, "points");
        tableHeaderMap.put(4, "time");
        tableHeaderMap.put(5, "title"); //TODO
    }
}
