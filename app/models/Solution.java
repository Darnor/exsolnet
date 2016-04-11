package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="solution")
public class Solution extends Post{
    @Basic
    private boolean official = false;

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

    public Solution(User user, Exercise exercise) {
        this.user = user;
        this.exercise = exercise;
        reports = new ArrayList<>();
        comments = new ArrayList<>();
        votes = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }
}
