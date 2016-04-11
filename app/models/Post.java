package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
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
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time = LocalDateTime.now();

    @Basic
    private int points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Post post = (Post) o;

        return getId() != null ? getId().equals(post.getId()) : post.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
