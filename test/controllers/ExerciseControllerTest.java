package controllers;

import models.AbstractModelTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExerciseControllerTest extends AbstractModelTest {

    @Test
    public void testGetOrderByAttributeString(){
        assertEquals("title", ExerciseController.getOrderByAttributeString(1));
        assertEquals("solutionCount", ExerciseController.getOrderByAttributeString(2));
        assertEquals("points", ExerciseController.getOrderByAttributeString(3));
        assertEquals("time", ExerciseController.getOrderByAttributeString(4));
    }
}
