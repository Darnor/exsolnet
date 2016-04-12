package models;

import com.avaje.ebean.PagedList;
import com.avaje.ebean.enhance.agent.SysoutMessageOutput;
import com.gargoylesoftware.htmlunit.javascript.host.Console;
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
        Tag tag = TagBuilder.aTag().withName("tag").withId(1L).withIsMainTag(true).build();
        exercise.addTag(tag);
        assertEquals(1,exercise.getTags().size());
        assertEquals("tag",exercise.getTags().get(0).getName());
        assertEquals(true,exercise.getTags().get(0).isMainTag());
    }
    @Test
    public void testFindExerciseData(){
        assertEquals("Basic Math",Exercise.findExerciseData(8000L).getTitle());
        assertEquals(null, Exercise.findExerciseData(404L));
    }
    @Test
    public void testRemoveTagIfNotInList(){
        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        List<Tag> tags = new ArrayList<Tag>();
        Tag t1 = TagBuilder.aTag().withMaintag(true).withName("aa").withId(1L).build();
        Tag t2 = TagBuilder.aTag().withMaintag(true).withName("cc").withId(2L).build();
        Tag t3 = TagBuilder.aTag().withMaintag(false).withName("bb").withId(3L).build();
        Tag t4 = TagBuilder.aTag().withMaintag(false).withName("dd").withId(4L).build();
        tags.add(t1);
        tags.add(t2);
        tags.add(t3);
        tags.add(t4);
        Exercise exercise = ExerciseBuilder.anExercise().withTags(tags).withId(1L).build();
        t1.addExercise(exercise);
        t2.addExercise(exercise);
        t3.addExercise(exercise);
        t4.addExercise(exercise);
        exercise.removeTagIfNotInList(list);
        assertEquals(2,exercise.getTags().size());
        assertEquals("aa",exercise.getTags().get(0).getName());
        assertEquals("bb",exercise.getTags().get(1).getName());
    }
    @Test
    public void testBindTag(){
        Exercise exercise = ExerciseBuilder.anExercise().build();
        Tag tag = TagBuilder.aTag().withName("bla").build();
        Exercise.bindTag(exercise,tag);
        assertEquals(1,exercise.getTags().size());
        assertEquals(1,tag.getExercises().size());
    }
    @Test
    public void testOtherTagExistsInExercise(){
        assertEquals(false,Exercise.otherTagExistsInExercise(8000L,"An1I"));
        assertEquals(true,Exercise.otherTagExistsInExercise(8000L,"simple operations"));
        assertEquals(false,Exercise.otherTagExistsInExercise(8000L,"foo"));

    }
    @Test
    public void testMainTagExistsInExercise(){
        assertEquals(false,Exercise.mainTagExistsInExercise(8000L,"simple operations"));
        assertEquals(true,Exercise.mainTagExistsInExercise(8000L,"An1I"));
        assertEquals(false,Exercise.mainTagExistsInExercise(8000L,"foo"));

    }
}
