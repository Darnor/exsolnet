package models;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="solution")
public class Solution  extends  Post{
    private Boolean official;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "solution")
    private List<Report> reports;

    @OneToMany(mappedBy = "solution")
    private List<Comment> comments;

    @OneToMany(mappedBy = "solution")
    private List<Vote> votes;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public static class SolutionBuilder {
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
}
