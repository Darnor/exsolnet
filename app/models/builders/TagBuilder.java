package models.builders;

import models.Exercise;
import models.Tag;

import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class TagBuilder {
    private Long id;
    private String name;
    private List<Exercise> exercises;

    private TagBuilder() {
    }

    public static TagBuilder aTag() {
        return new TagBuilder();
    }

    public TagBuilder withId(Long id) {
        this.id = id;
        return this;
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
        return aTag().withId(id).withName(name).withExercises(exercises);
    }

    public Tag build() {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setExercises(exercises);
        return tag;
    }
}
