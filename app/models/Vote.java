package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="vote")
public class Vote extends Model {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int value;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

   }
