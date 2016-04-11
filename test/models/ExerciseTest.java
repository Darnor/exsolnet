package models;

import com.avaje.ebean.PagedList;
import models.builders.ExerciseBuilder;
import models.builders.TagBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static models.builders.ExerciseBuilder.anExercise;
import static models.builders.SolutionBuilder.aSolution;
import static models.builders.TagBuilder.aTag;
import static models.builders.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
    }

    @Test
    public void testGetOrderByAttributeString(){
        assertEquals("title",Exercise.getOrderByAttributeString(1));
        assertEquals("solutionCount",Exercise.getOrderByAttributeString(2));
        assertEquals("points",Exercise.getOrderByAttributeString(3));
        assertEquals("time",Exercise.getOrderByAttributeString(4));
    }

    @Test
    public void testAddTag(){
        Exercise exercise = ExerciseBuilder.anExercise().build();
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        exerciseList.add(exercise);
        Tag tag = TagBuilder.aTag().withName("tag").withMaintag(true).withExercises(exerciseList).build();
        exercise.addTag(tag);
        assertEquals("tag",exercise.getTags().get(0).getName());
        assertEquals(true,exercise.getTags().get(0).isMainTag());
        assertEquals(1,exercise.getTags().size());
        assertEquals(1,exercise.getTags().get(0).getExercises().size());
    }
    @Test
    public void testFindExerciseData(){
        assertEquals("Basic Math",Exercise.findExerciseData(8000L).getTitle());
        assertEquals(null, Exercise.findExerciseData(404L));
    }
    @Test
    public void testRemoveTagIfNotInList(){
    }
    @Test
    public void testBindTag(){
        Exercise exercise = ExerciseBuilder.anExercise().withId(999L).build();
        Tag tag = TagBuilder.aTag().withId(333L).build();
        exercise.bindTag(tag);
        assertEquals(1,exercise.getTags().size());
        assertEquals(1,exercise.getTags().get(0).getExercises().size());
        assertTrue(333L==exercise.getTags().get(0).getId());
        assertTrue(999L==exercise.getTags().get(0).getExercises().get(0).getId());
    }
    @Test
    public void testOtherTagExistsInExercise(){
        assertEquals(true,Exercise.otherTagExistsInExercise(8000L,"An1I"));
        assertEquals(false,Exercise.otherTagExistsInExercise(8000L,"simple operations"));
        assertEquals(false,Exercise.otherTagExistsInExercise(8000L,"foo"));

    }
    @Test
    public void testMainTagExistsInExercise(){
        assertEquals(true,Exercise.otherTagExistsInExercise(8000L,"simple operations"));
        assertEquals(false,Exercise.otherTagExistsInExercise(8000L,"An1I"));
        assertEquals(false,Exercise.otherTagExistsInExercise(8000L,"foo"));

    }
}
