package models.builders;

import models.Solution;
import models.User;

import java.sql.Date;

/**
 * Created by tourn on 7.4.16.
 */
public class SolutionBuilder {
    private User user;
    private Long id;
    private String content;
    private Date time;
    private int points;

    private SolutionBuilder() {
    }

    public static SolutionBuilder aSolution() {
        return new SolutionBuilder();
    }

    public SolutionBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public SolutionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public SolutionBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public SolutionBuilder withTime(Date time) {
        this.time = time;
        return this;
    }

    public SolutionBuilder withPoints(int points) {
        this.points = points;
        return this;
    }

    public SolutionBuilder but() {
        return aSolution().withUser(user).withId(id).withContent(content).withTime(time).withPoints(points);
    }

    public Solution build() {
        Solution solution = new Solution();
        solution.setUser(user);
        solution.setId(id);
        solution.setContent(content);
        solution.setTime(time);
        solution.setPoints(points);
        return solution;
    }
}
