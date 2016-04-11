package models;

import com.avaje.ebean.PagedList;
import org.junit.Test;

import java.util.Arrays;

import static models.builders.ExerciseBuilder.anExercise;
import static models.builders.SolutionBuilder.aSolution;
import static models.builders.TagBuilder.aTag;
import static models.builders.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;

/**
 * Created by Frank on 11.04.2016.
 */
public class ExerciseTest extends AbstractModelTest {

    @Test
    public void testGetPagedListFilteredByTwoTags(){
        PagedList<Exercise> page = Exercise.getPagedList(0, "title", "", new String[]{"An1I", "simple operations"}, 10);
        assertEquals(2, page.getList().size());
        assertEquals("Basic Math", page.getList().get(0).getTitle());
        assertEquals("XXX", page.getList().get(1).getTitle());
    }
    @Test
    public void testGetPagedOrderedByTitleDesc(){
        PagedList<Exercise> page = Exercise.getPagedList(0, "title desc", "", new String[]{""}, 5);
        assertEquals(5, page.getList().size());
        assertEquals("XXX", page.getList().get(0).getTitle());
        assertEquals("Unit Testing", page.getList().get(1).getTitle());
    }
    @Test
    public void testGetPagedOrderedByRatingDesc(){
        PagedList<Exercise> page = Exercise.getPagedList(0, "points desc", "", new String[]{""}, 5);
        assertEquals(5, page.getList().size());
        assertEquals("Error Handling Design", page.getList().get(0).getTitle());
        assertEquals("Basic Math", page.getList().get(1).getTitle());
    }

    @Test
    public void testGetOrderByAttributeString(){
        assertEquals("title",Exercise.getOrderByAttributeString(1));
        assertEquals("solutionCount",Exercise.getOrderByAttributeString(2));
        assertEquals("points",Exercise.getOrderByAttributeString(3));
        assertEquals("time",Exercise.getOrderByAttributeString(4));
    }
}
