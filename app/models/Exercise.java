package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

/**
 * Created by mario on 21.03.16.
 */

@Entity
public class Exercise extends Model {

    @Id
    private Long id;

    @Constraints.Required
    private String title;

    @Basic
    private String content;

    @Basic
    private Date time;

    @Basic
    private int points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public static void create(Exercise exercise){
        exercise.save();
    }

    public static Finder<Long, Exercise> find = new Finder<Long,Exercise>(Exercise.class);
}
