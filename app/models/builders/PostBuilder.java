package models.builders;

import models.Post;

import java.sql.Date;

/**
 * Created by tourn on 7.4.16.
 */
public class PostBuilder {
    private Long id;
    private String content;
    private Date time;
    private int points;

    private PostBuilder() {
    }

    public static PostBuilder aPost() {
        return new PostBuilder();
    }

    public PostBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public PostBuilder withTime(Date time) {
        this.time = time;
        return this;
    }

    public PostBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public PostBuilder but() {
        return aPost().withId(id).withContent(content).withTime(time).withPoints(points);
    }

    public Post build() {
        Post post = new Post();
        post.setId(id);
        post.setContent(content);
        post.setTime(time);
        post.setPoints(points);
        return post;
    }
}
