package repositories;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.Query;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Exercise;
import models.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mario on 31.03.16.
 */

@Singleton
public class ExerciseRepository {
    private static final Model.Finder<Long, Exercise> find = new Model.Finder<>(Exercise.class);

    @Inject
    public ExerciseRepository() {
        //noop
    }

    public void create(Exercise exercise) {
        exercise.save();
    }

    public void update(Exercise exercise) {
        exercise.update();
    }

    public Model.Finder<Long, Exercise> find() {
        return find;
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
    public PagedList<Exercise> getPagedList(int pageNr, String orderBy, String titleFilter, String tagFilter, int pageSize) {
        //TODO: including tagFilter
        return this.find().where().contains("title", titleFilter).orderBy(orderBy).findPagedList(pageNr, pageSize);
    }

    public Exercise findExerciseData(Long id) {
        return this.find().where().eq("id", id).findUnique();
    }

    public String getOrderByAttributeString(int order){
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

