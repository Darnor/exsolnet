package models.builders;

import models.Exercise;
import models.Tag;
import models.Tracking;

import java.util.List;

public class TagBuilder {
    private Long id;
    private String name;
    private String longname;
    private boolean isMainTag;
    private List<Exercise> exercises;
    private List<Tracking> trackings;

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

    public TagBuilder withLongName(String longname) {
        this.longname = longname;
        return this;
    }

    public TagBuilder withIsMainTag(boolean isMainTag) {
        this.isMainTag = isMainTag;
        return this;
    }

    public TagBuilder withExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        return this;
    }

    public TagBuilder withTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
        return this;
    }

    public TagBuilder but() {
        return aTag().withId(id).withName(name).withLongName(longname).withIsMainTag(isMainTag).withExercises(exercises).withTrackings(trackings);
    }

    public Tag build() {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        tag.setLongName(longname);
        tag.setIsMainTag(isMainTag);
        tag.setExercises(exercises);
        tag.setTrackings(trackings);
        return tag;
    }
}
