package models.builders;

import models.Comment;
import models.Exercise;
import models.Solution;
import models.User;

import java.time.LocalDateTime;

public class CommentBuilder {
    private Long id;
    private String content;
    private User user;
    private Solution solution;
    private Exercise exercise;
    private LocalDateTime time = LocalDateTime.now();

    private CommentBuilder() {
    }

    public static CommentBuilder aComment() {
        return new CommentBuilder();
    }

    public CommentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CommentBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public CommentBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public CommentBuilder withSolution(Solution solution) {
        this.solution = solution;
        return this;
    }

    public CommentBuilder withExercise(Exercise exercise) {
        this.exercise = exercise;
        return this;
    }

    public CommentBuilder withTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public CommentBuilder but() {
        return aComment().withId(id).withContent(content).withUser(user).withSolution(solution).withExercise(exercise).withTime(time);
    }

    public Comment build() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setUser(user);
        comment.setSolution(solution);
        comment.setExercise(exercise);
        comment.setTime(time);
        return comment;
    }
}
