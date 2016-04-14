package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="track")
public class Tracking extends Model {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @NotNull
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Tracking(Long id, Tag tag, User user) {
        this.id = id;
        this.tag = tag;
        this.user = user;
    }

    public Long getTrackingId() {
        return id;
    }

    public Tag getTrackedTag() {
        return tag;
    }

    public void track() {
        id = null;
        save();
        user.track(this);
        tag.track(this);
    }

    public void unTrack() {
        delete();
        user.unTrack(id);
        tag.unTrack(id);
    }
}
