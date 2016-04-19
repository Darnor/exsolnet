package models;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static models.builders.ExerciseBuilder.anExercise;
import static models.builders.SolutionBuilder.aSolution;
import static models.builders.TagBuilder.aTag;
import static models.builders.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;

public class TagTest extends AbstractModelTest {

    @Test
    public void testGetCompletedExercisesNoSolutions(){
        User userA = anUser().withId(1L).withEmail("Hans").build();
        Tag tag = aTag().withId(1L).withName("A").build();
        Exercise e1 = anExercise().withId(1L).withTitle("Exercise 1").withTags(Arrays.asList(tag)).build();
        Exercise e2 = anExercise().withId(2L).withTitle("Exercise 2").withTags(Arrays.asList(tag)).build();
        assertEquals(0, userA.getNofCompletedExercisesByTag(tag));
    }

    @Test
    public void testGetCompletedExercisesNoExercises(){
        User userA = anUser().withId(1L).withEmail("Hans").build();
        Tag tag = aTag().withId(2L).withName("A").withExercises(Arrays.asList()).build();
        assertEquals(0, userA.getNofCompletedExercisesByTag(tag));
    }

    @Test
    public void testGetCompletedExercisesOnlySolution(){
        User userA = anUser().withId(1L).withEmail("Hans").build();
        Solution s1 = aSolution().withId(1L).withUser(userA).build();
        Solution s2 = aSolution().withId(2L).withUser(userA).build();
        userA.setSolutions(Arrays.asList(s1, s2));
        Tag tag = aTag().withId(1L).withName("A").build();
        Exercise e1 = anExercise().withId(1L).withTitle("Exercise 1").withSolutions(Arrays.asList(s1)).withTags(Arrays.asList(tag)).build();
        Exercise e2 = anExercise().withId(2L).withTitle("Exercise 2").withSolutions(Arrays.asList(s2)).withTags(Arrays.asList(tag)).build();
        Exercise e3 = anExercise().withId(3L).withTitle("Exercise 3").withSolutions(Arrays.asList()).withTags(Arrays.asList(tag)).build();
        s1.setExercise(e1);
        s2.setExercise(e2);
        tag.setExercises(Arrays.asList(e1, e2, e3));
        assertEquals(2, userA.getNofCompletedExercisesByTag(tag));
    }

    @Test
    public void testGetCompletedExercisesSeveralSolutions(){
        User userA = anUser().withId(1L).withEmail("Hans").build();
        User userB = anUser().withId(2L).withEmail("Franz").build();
        Solution s11 = aSolution().withId(1L).withUser(userA).build();
        Solution s12 = aSolution().withId(2L).withUser(userB).build();
        Solution s21 = aSolution().withId(3L).withUser(userA).build();
        Solution s22 = aSolution().withId(4L).withUser(userB).build();
        Solution s32 = aSolution().withId(5L).withUser(userB).build();
        userA.setSolutions(Arrays.asList(s11, s21));
        userB.setSolutions(Arrays.asList(s12, s22, s32));
        Tag tag = aTag().withId(1L).withName("A").build();
        Tag tag2 = aTag().withId(2L).withName("B").build();
        Exercise e1 = anExercise().withId(1L).withTitle("Exercise 1").withSolutions(Arrays.asList(s11, s12)).withTags(Arrays.asList(tag, tag2)).build();
        Exercise e2 = anExercise().withId(2L).withTitle("Exercise 2").withSolutions(Arrays.asList(s21, s22)).withTags(Arrays.asList(tag)).build();
        Exercise e3 = anExercise().withId(3L).withTitle("Exercise 3").withSolutions(Arrays.asList(s32)).withTags(Arrays.asList(tag, tag2)).build();
        s11.setExercise(e1);
        s12.setExercise(e1);
        s21.setExercise(e2);
        s22.setExercise(e2);
        s32.setExercise(e3);
        tag.setExercises(Arrays.asList(e1, e2, e3));
        tag2.setExercises(Arrays.asList(e1, e3));
        assertEquals(2, userA.getNofCompletedExercisesByTag(tag));
    }

    @Test
    public void testGetSuggestedTags(){
        assertEquals(1, Tag.getSuggestedTags("An1").size());
        assertEquals("An1I", Tag.getSuggestedTags("An1").get(0).getName());
        assertEquals(2, Tag.getSuggestedTags("An").size());
        List<Tag> suggestedTags = Tag.getSuggestedTags("a");
        assertEquals(7, suggestedTags.size());
        assertEquals("An1I", suggestedTags.get(0).getName());
    }


    @Test
    public void testGetSuggestedMainTags(){
        assertEquals(1, Tag.getSuggestedTags("V", true).size());
        assertEquals("VSS", Tag.getSuggestedTags("V", true).get(0).getName());
    }

    @Test
    public void testGetSuggestedOtherTags(){
        assertEquals(1, Tag.getSuggestedTags("Abl", false).size());
        assertEquals("Ableiten", Tag.getSuggestedTags("Abl", false).get(0).getName());
    }

    @Test
    public void testFindTagByName(){
        assertEquals("An1I", Tag.findTagByName("An1I").getName());
        assertEquals(true, Tag.findTagByName("An1I").isMainTag());
        assertEquals(null, Tag.findTagByName("asdfsadf"));
    }

    @Test
    public void testGetSuggestedTagsNotExists(){
        List<Tag> suggestedTags = Tag.getSuggestedTags("b");
        assertEquals(0, suggestedTags.size());
    }
}
