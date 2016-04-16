package models;

import com.avaje.ebean.Model;
import models.builders.TagBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
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

    @Column(unique = true)
    @NotNull
    private String name;

    @NotNull
    private boolean isMainTag;

    @ManyToMany(mappedBy = "tags")
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = "tag_id")
    private List<Tracking> trackings;

    private static final String COLUMN_IS_MAIN_TAG = "isMainTag";
    private static final String COLUMN_TAG_NAME = "name";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMainTag() {
        return isMainTag;
    }

    public void setIsMainTag(boolean isMainTag) {
        this.isMainTag = isMainTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Tracking> getTrackings() {
        return Collections.unmodifiableList(trackings);
    }

    public void setTrackings(List<Tracking> trackings) {
        this.trackings = trackings;
    }

    public static Model.Finder<Long, Tag> find() {
        return new Finder<>(Tag.class);
    }

    public long getNofCompletedExercises(User currentUser) {
        return getExercises().stream().mapToLong(exercise ->
                exercise.getSolutions().stream().filter(solution -> solution.getUser().equals(currentUser)).count()
        ).sum();
    }

    public static List<Tag> process(String[] tagNames, boolean isMainTag) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = Tag.find().all().stream().filter(t -> t.getName().equals(tagName)).findFirst().orElse(TagBuilder.aTag().withName(tagName).withIsMainTag(isMainTag).build());
            if (tag.getId() == null) {
                tag.save();
            }
            tags.add(tag);
        }
        return tags;
    }

    /**
     * returns list of suggested tags (other and main)
     * case-insensitiv
     *
     * @param tagName tag that starts with tagName
     * @return list of all tags that start with tagName
     */
    public static List<Tag> getSuggestedTags(String tagName) {
        return find().where().istartsWith(COLUMN_TAG_NAME, tagName).findList();
    }

    /**
     * returns list of suggested tags which are main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of main tags that start with tagName
     */
    public static List<Tag> getSuggestedTags(String tagName, boolean isMainTag) {
        List<Tag> list = find().where().eq(COLUMN_IS_MAIN_TAG, isMainTag).istartsWith(COLUMN_TAG_NAME, tagName).findList();
        return list.isEmpty() ? find().where().eq(COLUMN_IS_MAIN_TAG, isMainTag).findList() : list;
    }

    /**
     * returns the tag searched by name
     *
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().eq(COLUMN_TAG_NAME, name).findUnique();
    }
}
