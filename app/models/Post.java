package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Claudia on 31.03.2016.
 */
@MappedSuperclass
public class Post extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @NotNull
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private LocalDateTime time = LocalDateTime.now();

    @Basic
    private long points;

    public Post() {
        this.id = null;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getPoints() {
        return points;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
