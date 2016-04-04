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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Tag getTag() {
        return tag;
    }
}
