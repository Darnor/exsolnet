package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="comment")
public class Comment extends Model{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "comment")
    private List<Report> reports;

    @ManyToOne
    @JoinColumn(name="solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time = LocalDateTime.now();


    private static final int NOF_RECENT_COMMENTS = 5;


    public String getContent() {
        return content;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


    /**
     * @param user
     * @return a list of recent comments that have been added to user's posts
     */
    public static List<Comment> getRecentComments(User user) {
        return find().where()
                .eq("user", user)
                .orderBy("time desc")
                .setMaxRows(NOF_RECENT_COMMENTS)
                .findList();
    }

    public static Model.Finder<Long, Comment> find(){
        return new Model.Finder<>(Comment.class);
    }

}
