package models;

import helper.AbstractApplicationTest;
import helper.DatabaseHelper;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SolutionTest extends AbstractApplicationTest {

    @Test
    @Ignore("Claudia pls fix")
    public void testCreateSolutions() {
        Solution.create("content", Exercise.findValidById(8000L), User.findByUsername("Franz"));
        List<Solution> fromdb = Exercise.findValidById(8000L).getSolutions();
        assertEquals("It's obviously 4", fromdb.get(0).getContent());
        assertEquals("content", fromdb.get(1).getContent());
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

}
