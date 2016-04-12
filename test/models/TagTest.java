package models;

import org.junit.Test;

import java.util.Arrays;

import static models.builders.ExerciseBuilder.anExercise;
import static models.builders.SolutionBuilder.aSolution;
import static models.builders.TagBuilder.aTag;
import static models.builders.UserBuilder.anUser;
import static org.junit.Assert.assertEquals;

/**
 * Created by tourn on 5.4.16.
 */
public class TagTest extends AbstractModelTest {

    @Test
    public void testGetCompletedExercisesNoSolutions() {
        User userA = anUser().withEmail("Hans").build();
        Exercise e1 = anExercise().withTitle("Exercise 1").build();
        Exercise e2 = anExercise().withTitle("Exercise 2").build();
        Tag tag = aTag().withName("A").withExercises(Arrays.asList(e1, e2)).build();
        assertEquals(0, tag.getNofCompletedExercises(userA));
    }

    @Test
    public void testGetCompletedExercisesNoExercises() {
        User userA = anUser().withEmail("Hans").build();
        Tag tag = aTag().withName("A").withExercises(Arrays.asList()).build();
        assertEquals(0, tag.getNofCompletedExercises(userA));
    }

    @Test
    public void testGetCompletedExercisesOnlySolution() {
        User userA = anUser().withEmail("Hans").build();
        Solution s1 = aSolution().withUser(userA).build();
        Solution s2 = aSolution().withUser(userA).build();
        Exercise e1 = anExercise().withTitle("Exercise 1").withSolutions(Arrays.asList(s1)).build();
        Exercise e2 = anExercise().withTitle("Exercise 2").withSolutions(Arrays.asList(s2)).build();
        Exercise e3 = anExercise().withTitle("Exercise 3").withSolutions(Arrays.asList()).build();
        Tag tag = aTag().withName("A").withExercises(Arrays.asList(e1, e2, e3)).build();
        assertEquals(2, tag.getNofCompletedExercises(userA));
    }

    @Test
    public void testGetCompletedExercisesSeveralSolutions() {
        User userA = anUser().withEmail("Hans").build();
        User userB = anUser().withEmail("Franz").build();
        Solution s11 = aSolution().withUser(userA).build();
        Solution s12 = aSolution().withUser(userB).build();
        Solution s21 = aSolution().withUser(userA).build();
        Solution s22 = aSolution().withUser(userB).build();
        Solution s32 = aSolution().withUser(userB).build();
        Exercise e1 = anExercise().withTitle("Exercise 1").withSolutions(Arrays.asList(s11, s12)).build();
        Exercise e2 = anExercise().withTitle("Exercise 2").withSolutions(Arrays.asList(s21, s22)).build();
        Exercise e3 = anExercise().withTitle("Exercise 3").withSolutions(Arrays.asList(s32)).build();
        Tag tag = aTag().withName("A").withExercises(Arrays.asList(e1, e2, e3)).build();
        assertEquals(2, tag.getNofCompletedExercises(userA));
    }

    @Test
    public void testGetSuggestedTags() {
        assertEquals(1, Tag.getSuggestedTags("An1").size());
        assertEquals("An1I", Tag.getSuggestedTags("An1").get(0).getName());
        assertEquals(2, Tag.getSuggestedTags("An").size());

        java.util.List<Tag> suggestedTags = Tag.getSuggestedTags("a");
        assertEquals(7, suggestedTags.size());
        assertEquals("An1I", suggestedTags.get(0).getName());
    }

    @Test
    public void testGetSuggestedMainTags() {
        assertEquals(1, Tag.getSuggestedMainTags("V").size());
        assertEquals("VSS", Tag.getSuggestedMainTags("V").get(0).getName());
    }

    @Test
    public void testGetSuggestedOtherTags() {
        assertEquals(1, Tag.getSuggestedOtherTags("Abl").size());
        assertEquals("Ableiten", Tag.getSuggestedOtherTags("Abl").get(0).getName());
    }

    @Test
    public void testFindTagByName() {
        assertEquals("An1I", Tag.findTagByName("An1I").getName());
        assertEquals(true, Tag.findTagByName("An1I").isMainTag());
        assertEquals(null, Tag.findTagByName("asdfsadf"));
    }


    @Test
    public void testFindMainTagByName() {
        assertEquals("An1I", Tag.findMainTagByName("An1I").getName());
        assertEquals(true, Tag.findMainTagByName("An1I").isMainTag());
        assertEquals(null, Tag.findMainTagByName("asdfsadf"));
        assertEquals(null, Tag.findMainTagByName("Ableiten"));
    }


    @Test
    public void testFindOtherTagByName() {
        assertEquals("Ableiten", Tag.findOtherTagByName("Ableiten").getName());
        assertEquals(false, Tag.findOtherTagByName("Ableiten").isMainTag());
        assertEquals(null, Tag.findOtherTagByName("asdfsadf"));
        assertEquals(null, Tag.findOtherTagByName("An1I"));
    }

    @Test
    public void testGetSuggestedTagsNotExists() {
        java.util.List<Tag> suggestedTags = Tag.getSuggestedTags("b");
        assertEquals(0, suggestedTags.size());
    }
}
