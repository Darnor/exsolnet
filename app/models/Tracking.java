package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="track")
public class Tracking extends Model{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Tracking(Tag tag, User user) {
        this.tag = tag;
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public User getUser() {
        return user;
    }
}
