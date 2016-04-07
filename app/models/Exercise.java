package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.annotation.Formula;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mario on 21.03.16.
 */

@Entity
@Table(name = "exercise")
public class Exercise extends Post {

    @Constraints.Required
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
    private User user;

    @OneToMany(mappedBy = "exercise")
    private List<Report> reports;

    @OneToMany(mappedBy = "exercise")
    private List<Comment> comments;

    public void addTag(Tag tag){
        tags.add(tag);
    }
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

    public static void create(Exercise exercise) {
        exercise.save();
    }

    public static void update(Exercise exercise) {
        exercise.update();
    }

    public static Model.Finder<Long, Exercise> find() {
        return new Finder<Long, Exercise>(Exercise.class);
    }

    /**
     * Returns a Paged List
     *
     * @param pageNr      the page number
     * @param orderBy     TODO
     * @param titleFilter the string which is used for filter/query the title
     * @param tagFilter   TODO
     * @param pageSize    the count of exercises for one page
     * @return the PagedList for the actual page and filters/orders
     */
    public static PagedList<Exercise> getPagedList(int pageNr, String orderBy, String titleFilter, String tagFilter, int pageSize) {
        //TODO: including tagFilter
        return find().where().contains("title", titleFilter).orderBy(orderBy).findPagedList(pageNr, pageSize);
    }

    public static Exercise findExerciseData(Long id) {
        return find().where().eq("id", id).findUnique();
    }

    public static String getOrderByAttributeString(int order){
        String result = tableHeaderMap.get(Math.abs(order));
        if(order<0){
            result += " desc";
        }
        return result;
    }

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
