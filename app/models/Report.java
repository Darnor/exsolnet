package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="report")
public class Report extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
   @JoinColumn(name = "comment_id")
    private Comment comment;

    public Report(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
