package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="report")
public class Report extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ExsolnetUser user;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
   @JoinColumn(name = "comment_id")
    private Comment comment;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExsolnetUser getUser() {
        return user;
    }

    public void setUser(ExsolnetUser user) {
        this.user = user;
    }


}
