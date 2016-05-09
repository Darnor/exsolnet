package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastchanged;

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

    public LocalDateTime getLastChanged() {
        return lastchanged;
    }

    public void setLastChanged(LocalDateTime time) {
        this.lastchanged = lastchanged;
    }
}
