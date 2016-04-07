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

    public static class TagBuilder {
        private String name;
        private List<Exercise> exercises;

        private TagBuilder() {
        }

        public static TagBuilder aTag() {
            return new TagBuilder();
        }

        public TagBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TagBuilder withExercises(List<Exercise> exercises) {
            this.exercises = exercises;
            return this;
        }

        public TagBuilder but() {
            return aTag().withName(name).withExercises(exercises);
        }

        public Tag build() {
            Tag tag = new Tag();
            tag.setName(name);
            tag.setExercises(exercises);
            return tag;
        }
    }
}
