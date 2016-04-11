package models.builders;

import models.Exercise;
import models.Tag;

import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class TagBuilder {
    private String name;
    private boolean isMainTag;
    private List<Exercise> exercises;

    private TagBuilder() {
    }

    public static TagBuilder aTag() {
        return new TagBuilder();
    }

    public TagBuilder withIsMainTag(boolean isMainTag) {
        this.isMainTag = isMainTag;
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
        return aTag().withName(name).withIsMainTag(isMainTag).withExercises(exercises);
    }

    public Tag build() {
        Tag tag = new Tag(name, isMainTag);
        tag.setName(name);
        for (Exercise exercise : exercises) {
            tag.addExercise(exercise);
        }
        return tag;
    }
}
