package models;

import helper.AbstractApplicationTest;
import helper.DatabaseHelper;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class CommentTest extends AbstractApplicationTest {


    @Test
    public void findACommentById() {
        Comment comment = Comment.findValidById(8001L);
        Assert.assertEquals("HOW CAN YOU BE SO WRONG!", comment.getContent());
    }

    @Test
    public void createNewCommentForSolution() {
        Comment comment = Comment.create("content", Solution.findById(8013L), User.findByUsername("Franz"));
        Assert.assertEquals(Comment.findValidById(comment.getId()).getContent(), "content");
        Assert.assertEquals(Comment.findValidById(comment.getId()).getUser().getUsername(), "Franz");
        Assert.assertEquals(Comment.findValidById(comment.getId()).getSolution().getId().toString(), "8013");
    }

    @Test
    public void createNewCommentForExercise() {
        Comment comment = Comment.create("content", Exercise.findById(8004L), User.findByUsername("Franz"));
        Assert.assertEquals(Comment.findValidById(comment.getId()).getContent(), "content");
        Assert.assertEquals(Comment.findValidById(comment.getId()).getUser().getUsername(), "Franz");
        Assert.assertEquals(Comment.findValidById(comment.getId()).getExercise().getId().toString(), "8004");
    }

    @Test
    public void updateCommentFromExercise() {
        Comment old = Comment.findValidById(8002L);
        Comment updated = Comment.updateContent(8002L, old.getContent() + "new");
        assertSameComment(old,updated);

    }

    @Test
    public void updateCommentFromSolution() {
        Comment old = Comment.findValidById(8000L);
        Comment updated = Comment.updateContent(8000L, old.getContent() + "new");
        assertSameComment(old,updated);
    }

    private void assertSameComment(Comment old, Comment updated){
        Assert.assertEquals(old.getContent()+"new",updated.getContent());
        Assert.assertEquals(old.getExercise(),updated.getExercise());
        Assert.assertEquals(old.getId(),updated.getId());
        Assert.assertEquals(old.getUser(),updated.getUser());
        Assert.assertEquals(old.getSolution(),updated.getSolution());
        Assert.assertEquals(old.getTime(),updated.getTime());
    }

    @Test
    public void testCommentDelete(){
        long commentToDelete = 8000L;
        assertNotNull(Comment.findValidById(commentToDelete));
        Comment.delete(commentToDelete);
        assertNull(Comment.findValidById(commentToDelete));

        DatabaseHelper.cleanDB(app);
    }



    @Test
    public void testCommentDeleteAndUndo(){
        long commentToDelete = 8000L;
        assertNotNull(Comment.findValidById(commentToDelete));
        Comment.delete(commentToDelete);
        assertNull(Comment.findValidById(commentToDelete));
        Comment.undoDelete(commentToDelete);
        assertNotNull(Comment.findValidById(commentToDelete));

        DatabaseHelper.cleanDB(app);
    }
}

