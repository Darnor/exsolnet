package models;

import helper.AbstractApplicationTest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SolutionTest extends AbstractApplicationTest {

    @Test
    @Ignore("Claudia pls fix")
    public void testCreateSolutions() {
        Solution.create("content", Exercise.findById(8000L), User.findUserByUsername("Franz"));
        List<Solution> fromdb = Exercise.findById(8000L).getSolutions();
        assertEquals("It's obviously 4", fromdb.get(0).getContent());
        assertEquals("content", fromdb.get(1).getContent());
    }

}
