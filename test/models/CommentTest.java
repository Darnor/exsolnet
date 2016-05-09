package models;

import helper.AbstractApplicationTest;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest extends AbstractApplicationTest {

    @Test
    public void findACommentById() {
        Comment comment = Comment.findById(8001L);
        Assert.assertEquals("HOW CAN YOU BE SO WRONG!", comment.getContent());
    }

    @Test
    public void createNewCommentForSolution() {
        Comment comment = Comment.create("content", Solution.findById(8013L), User.findByUsername("Franz"));
        Assert.assertEquals(Comment.findById(comment.getId()).getContent(), "content");
        Assert.assertEquals(Comment.findById(comment.getId()).getUser().getUsername(), "Franz");
        Assert.assertEquals(Comment.findById(comment.getId()).getSolution().getId().toString(), "8013");
    }

    @Test
    public void createNewCommentForExercise() {
        Comment comment = Comment.create("content", Exercise.findById(8004L), User.findByUsername("Franz"));
        Assert.assertEquals(Comment.findById(comment.getId()).getContent(), "content");
        Assert.assertEquals(Comment.findById(comment.getId()).getUser().getUsername(), "Franz");
        Assert.assertEquals(Comment.findById(comment.getId()).getExercise().getId().toString(), "8004");
    }

    @Test
    public void updateCommentFromExercise() {
        Comment old = Comment.findById(8002L);
        Comment updated = Comment.updateContent(8002L, old.getContent() + "new");
        assertSameComment(old,updated);

    }

    @Test
    public void updateCommentFromSolution() {
        Comment old = Comment.findById(8000L);
        Comment updated = Comment.updateContent(8000L, old.getContent() + "new");
        assertSameComment(old,updated);
    }

    private void assertSameComment(Comment old, Comment updated){
        Assert.assertEquals(old.getContent()+"new",updated.getContent());
        Assert.assertEquals(old.getExercise(),updated.getExercise());
        Assert.assertEquals(old.getId(),updated.getId());
        Assert.assertEquals(old.getUser(),updated.getUser());
        Assert.assertEquals(old.getSolution(),updated.getSolution());
        Assert.assertEquals(old.getReports(),updated.getReports());
        Assert.assertEquals(old.getTime(),updated.getTime());

    }
}

