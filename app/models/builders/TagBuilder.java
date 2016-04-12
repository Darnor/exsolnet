package models.builders;

import models.Exercise;
import models.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tourn on 7.4.16.
 */
public class TagBuilder {
    private String name;
    private boolean isMainTag;
    private List<Exercise> exercises;
    private Long id;

    private TagBuilder() {
        exercises = new ArrayList<Exercise>();
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

    public TagBuilder withId(long id) {
        this.id = id;
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
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setIsMainTag(isMainTag);
        for (Exercise exercise : exercises) {
            tag.addExercise(exercise);
        }
        return tag;
    }
}
