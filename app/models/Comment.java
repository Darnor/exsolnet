package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="comment")
public class Comment extends Model{
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private ExsolnetUser user;

    @OneToMany(mappedBy = "comment")
    private List<Report> reports;

    @ManyToOne
    @JoinColumn(name="solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name="exercise_id")
    private Exercise exercise;




}
