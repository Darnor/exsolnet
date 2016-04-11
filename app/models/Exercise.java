package models;

import com.avaje.ebean.*;
import com.avaje.ebean.Query;
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
        query.where().contains("title",titleFilter);
        if(!tagFilter[0].equals("")){
            query.where().in("tags.name",tagFilter);
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


    /**
     * Remove the tag from the exercise and the exercise from the tag if it
     * doesnt exist in the list
     * @param tags the tag list to be searched
     */
    public void removeTagIfNotInList(List<String> tags) {
        this.tags.forEach(t -> {
            if (!tags.contains(t.getName())) {
                t.removeExercise(getId());
            }
        });
        this.tags.removeIf(t -> !tags.contains(t.getName()));
    }

    /**
     * add exercise to tag list
     * add tag to exercise
     * @param tag the tag to be bind
     */
    public void bindTag(Tag tag) {
        tag.addExercise(this);
        addTag(tag);
    }

    /**
     * tells if the normal tag already is saved in the current exercise in the db
     *
     * @param id the exercise id
     * @param t  the name of the tag
     * @return true if it exists, false if its not yet saved
     */
    public static Boolean otherTagExistsInExercise(long id, String t) {

        List<Tag> tags = findExerciseData(id).getTags();
        for (Tag tag : tags) {
            if (tag.getName().equals(t) && !tag.isMainTag()) {
                return true;
            }
        }
        return false;
    }

    /**
     * tells if the maintag is saved in the current exercise in the db
     *
     * @param id the id of the exercise
     * @param t  the name of the id
     * @return true if the maintag exists, false if it doesnt exist
     */
    public static Boolean mainTagExistsInExercise(long id, String t) {
        List<Tag> tags = findExerciseData(id).getTags();
        for (Tag tag : tags) {
            if (tag.getName().equals(t) && tag.isMainTag())
                return true;
        }
        return false;
    }
}
