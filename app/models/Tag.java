package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Claudia on 31.03.2016.
 */
@Entity
@Table(name = "tag")
public class Tag extends Model {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    //TODO UNIQUE
    @NotNull
    private String name;

    private Boolean isMainTag;

    @ManyToMany(mappedBy = "tags")
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = "tag")
    private List<Tracking> trackings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMainTag() {
        return isMainTag;
    }

    public void setMainTag(Boolean mainTag) {
        isMainTag = mainTag;
    }

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

    public void removeExercise(Long id) {
        exercises.removeIf(e -> e.getId() == id);
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public static void create(Tag tag) {
        tag.save();
    }

    public static void update(Tag tag) {
        tag.update();
    }

    public static Model.Finder<Long, Tag> find() {
        return new Finder<Long, Tag>(Tag.class);
    }

    public long getNofCompletedExercises(User currentUser) {
        return getExercises().stream().mapToLong(exercise ->
                exercise.getSolutions().stream().filter(solution -> solution.getUser().equals(currentUser)).count()
        ).sum();
    }

    public static List<Tag> getSuggestedTags(String query) {
        return find().where().istartsWith("name", query).findList();
    }

    /**
     * returns the tag searched by name
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().eq("name", name).findUnique();
    }
}
