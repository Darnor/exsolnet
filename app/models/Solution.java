package models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="solution")
public class Solution  extends  Post{
    @Basic
    private Boolean official = false;

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


}
