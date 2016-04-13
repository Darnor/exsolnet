package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="track")
public class Tracking extends Model{
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

    @NotNull
    private boolean trackingStatus;

    public Tracking(Tag tag, User user) {
        this.id = null;
        this.tag = tag;
        this.user = user;
        this.trackingStatus = true;
    }

    public Long getId() {
        return id;
    }

    public Tag getTag() {
        return tag;
    }

    public User getUser() {
        return user;
    }

    public boolean getTrackingStatus() {
        return trackingStatus;
    }

    public void track() {
        trackingStatus = true;
    }

    public void unTrack() {
        trackingStatus = false;
    }
}
