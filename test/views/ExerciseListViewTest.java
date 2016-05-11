package views;

import com.avaje.ebean.PagedList;
import models.Exercise;
import org.junit.BeforeClass;
import org.junit.Test;
import play.twirl.api.Content;
import views.html.exerciseList;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ExerciseListViewTest extends AbstractViewTest {
    private static PagedList<Exercise> pagedExerciseList;
    private static PagedList<Exercise> pageZero;
    private static PagedList<Exercise> pageOne;

    @BeforeClass
    public static void setUpBeforeClass(){
        pagedExerciseList = Exercise.find().where().eq("valid", true).orderBy("title").findPagedList(0,2);
        pageZero = Exercise.find().where().eq("valid", true).orderBy("title").findPagedList(0,10);
        pageOne = Exercise.find().where().eq("valid", true).orderBy("title").findPagedList(1,10);
    }

    @Test
    public void testListContainsTwoElements() {
        Content html = exerciseList.render(null, pagedExerciseList, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("Ableitung 1a)"));
        assertThat(html.body(), containsString("AVL-Tree Suchkosten"));
    }

    @Test
    public void testTitleFilterExistInInputField() {
        Content html = exerciseList.render(null, pagedExerciseList, 1, "XXX", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("name=\"title\" value=\"XXX\""));
    }

    @Test
    public void testTagFilterExistInInputField() {
        Content html = exerciseList.render(null, pagedExerciseList, 1, "", "ABC,DEF");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("\"ABC,DEF\""));
    }

    @Test
    public void testPaginationPageZeroPrevDisabled() {
        Content html = exerciseList.render(null, pageZero, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<a class=\"btn prev disabled\""));
    }

    @Test
    public void testPaginationPageZeroNextNotDisabled() {
        Content html = exerciseList.render(null, pageZero, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<a class=\"btn next\""));
    }

    @Test
    public void testPaginationPageZeroText() {
        Content html = exerciseList.render(null, pageZero, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("1 bis 10 von 13"));
    }

    @Test
    public void testPaginationPageOnePrevNotDisabled() {
        Content html = exerciseList.render(null, pageOne, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<a class=\"btn prev\""));
    }

    @Test
    public void testPaginationPageOneNextDisabled() {
        Content html = exerciseList.render(null, pageOne, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("<a class=\"btn next disabled\""));
    }

    @Test
    public void testPaginationPageOneText() {
        Content html = exerciseList.render(null, pageOne, 1, "", "");
        assertEquals("text/html", html.contentType());
        assertThat(html.body(), containsString("11 bis 13 von 13"));
    }
}