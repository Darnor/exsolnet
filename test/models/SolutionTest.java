package models;

import helper.AbstractApplicationTest;
import helper.DatabaseHelper;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SolutionTest extends AbstractApplicationTest {

    @Test
    public void testCreateSolutions() {
        Exercise exercise = Exercise.findById(8000L);
        assertNotNull(exercise);
        int noOfSolutions = exercise.getValidSolutions().size();
        Solution.create("content", exercise, User.findByUsername("Franz"));
        List<Solution> fromdb = Exercise.findById(8000L).getValidSolutions();
        assertEquals(1, fromdb.stream().filter(s -> s.getContent().equals("content")).count());
        assertEquals(noOfSolutions + 1, fromdb.size());
    }

    @Test
    public void testPoints(){
        assertEquals(1, Solution.findValidById(8002L).getPoints());
        assertEquals(-5, Solution.findValidById(8012L).getPoints());
        assertEquals(-1, Solution.findValidById(8011L).getPoints());
    }

    @Test
    public void testSolutionDelete(){
        long solutionIdToDelete = 8000L;
        assertNotNull(Solution.findValidById(solutionIdToDelete));
        Solution.delete(solutionIdToDelete);
        assertNull(Solution.findValidById(solutionIdToDelete));

        DatabaseHelper.cleanDB(app);
    }

    @Test
    public void testSolutionDeleteAndUndo(){
        long solutionIdToDelete = 8000L;
        assertNotNull(Solution.findValidById(solutionIdToDelete));
        Solution.delete(solutionIdToDelete);
        assertNull(Solution.findValidById(solutionIdToDelete));
        Solution.undoDelete(solutionIdToDelete);
        assertNotNull(Solution.findValidById(solutionIdToDelete));

        DatabaseHelper.cleanDB(app);
    }

    @Test
    public void testEditSolution(){
        long solutionIdToEdit = 8001L;
        String newContent = "<p>Hans war hier</p>";
        assertNotNull(Solution.findValidById(solutionIdToEdit));
        assertNull(Solution.findValidById(solutionIdToEdit).getLastChanged());
        Solution.update(8001L,newContent);
        assertEquals(newContent,Solution.findValidById(solutionIdToEdit).getContent());
        assertNotNull(Solution.findValidById(solutionIdToEdit).getLastChanged());

        DatabaseHelper.cleanDB(app);
    }
}
