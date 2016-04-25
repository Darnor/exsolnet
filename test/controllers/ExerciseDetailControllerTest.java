package controllers;


import helper.AbstractApplicationTest;
import models.Exercise;
import models.Solution;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ExerciseDetailControllerTest extends AbstractApplicationTest {

    @Test
    @Ignore("Fix Solution getPoints first.")
    public void testSortSolutionsWithMoreThanTwoSolutionsAndOfficialSolution(){
        List<Solution> sortedSolutions = ExerciseDetailController.getSortedSolutionList(Exercise.findById(8005L).getSolutions());

        // First element should be official
        assertTrue(sortedSolutions.get(0).isOfficial());

        // Last element should be the worst
        assertThat(sortedSolutions.get(sortedSolutions.size()-1).getContent(),containsString("www.iamhans.com"));
    }
}
