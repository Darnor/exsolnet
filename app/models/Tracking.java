package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "track")
public class Tracking extends Model {

    static final String COLUMN_TAG_ID = "tag_id";
    static final String COLUMN_USER_ID = "user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = COLUMN_TAG_ID)
    @NotNull
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = COLUMN_USER_ID)
    @NotNull
    private User user;

    public static Tracking findById(Long id) {
        return find().byId(id);
    }

    public static Model.Finder<Long, Tracking> find() {
        return new Finder<>(Tracking.class);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
