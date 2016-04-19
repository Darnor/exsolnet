package models;

import com.avaje.ebean.PagedList;
import models.builders.ExerciseBuilder;
import models.builders.TagBuilder;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

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
    public void testAddTag(){
        Exercise exercise = ExerciseBuilder.anExercise().build();
        Tag tag = TagBuilder.aTag().withName("tag").withId(1L).withIsMainTag(true).build();
        exercise.setTags(Arrays.asList(tag));
        assertEquals(1,exercise.getTags().size());
        assertEquals("tag",exercise.getTags().get(0).getName());
        assertEquals(true,exercise.getTags().get(0).isMainTag());
    }

    @Test
    public void testFindExerciseData(){
        assertEquals("Basic Math",Exercise.findById(8000L).getTitle());
        assertEquals(null, Exercise.findById(404L));
    }

    @Test
    public void testTagsAreSorted(){
        Tag t1 = TagBuilder.aTag().withIsMainTag(true).withName("aa").withId(1L).build();
        Tag t2 = TagBuilder.aTag().withIsMainTag(true).withName("cc").withId(2L).build();
        Tag t3 = TagBuilder.aTag().withIsMainTag(false).withName("bb").withId(3L).build();
        Tag t4 = TagBuilder.aTag().withIsMainTag(false).withName("dd").withId(4L).build();
        Exercise exercise = ExerciseBuilder.anExercise().withTags(Arrays.asList(t1, t2, t3, t4)).withId(1L).build();
        assertEquals(4, exercise.getTags().size());
        assertEquals("aa", exercise.getTagsSortedByTagType().get(0).getName());
        assertEquals("cc", exercise.getTagsSortedByTagType().get(1).getName());
        assertEquals("bb", exercise.getTagsSortedByTagType().get(2).getName());
        assertEquals("dd", exercise.getTagsSortedByTagType().get(3).getName());
    }

    @Test
    public void testOtherTagExistsInExercise() {
        Exercise exercise = Exercise.findById(8000L);
        assertEquals(1, exercise.getTags().stream().filter(t -> t.getName().equals("simple operations") && !t.isMainTag()).count());
        assertEquals(0, exercise.getTags().stream().filter(t -> t.getName().equals("An1I") && !t.isMainTag()).count());
        assertEquals(0, exercise.getTags().stream().filter(t -> t.getName().equals("foo") && !t.isMainTag()).count());
    }

    @Test
    public void testMainTagExistsInExercise() {
        Exercise exercise = Exercise.findById(8000L);
        assertEquals(0, exercise.getTags().stream().filter(t -> t.getName().equals("simple operations") && t.isMainTag()).count());
        assertEquals(1, exercise.getTags().stream().filter(t -> t.getName().equals("An1I") && t.isMainTag()).count());
        assertEquals(0, exercise.getTags().stream().filter(t -> t.getName().equals("foo") && t.isMainTag()).count());
    }
}
