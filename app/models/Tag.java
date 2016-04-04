package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name="tag")
public class Tag extends Model {
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private Boolean isMainTag;

    @ManyToMany(mappedBy = "tags")
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = "tag")
    private List<Tracking> trackings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

   }
