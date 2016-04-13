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
    private Boolean isMainTag;

    @ManyToMany(mappedBy = "tags")
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = "tag")
    private List<Tracking> trackings;

    private static final String IS_MAIN_TAG_STR = "isMainTag";
    private static final String NAME_STR = "name";

    /**
     * Constructor for new Tag for one exercise
     *
     * @param name      the name of the tag
     * @param isMainTag is main tag or not
     */
    public Tag(String name, boolean isMainTag) {
        this.id = null;
        this.name = name;
        this.isMainTag = isMainTag;
        this.exercises = new ArrayList<>();
        this.trackings = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Boolean isMainTag() {
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

    public void removeExercise(Long exerciseId) {
        exercises.removeIf(e -> e.getId().equals(exerciseId));
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public List<Tracking> getTrackings() {
        return Collections.unmodifiableList(trackings);
    }

    public void addTracking(Tracking tracking) {
        trackings.add(tracking);
    }

    public Boolean getMainTag() {
        return isMainTag;
    }

    public void setMainTag(Boolean mainTag) {
        isMainTag = mainTag;
    }

    /**
     * Save tag in db
     *
     * @param tag to be saved
     */
    public static void create(Tag tag) {
        tag.save();
    }

    public static void update(Tag tag) {
        tag.update();
    }

    public static Model.Finder<Long, Tag> find() {
        return new Finder<>(Tag.class);
    }

    public long getNofCompletedExercises(User currentUser) {
        return getExercises().stream().mapToLong(exercise ->
                exercise.getSolutions().stream().filter(solution -> solution.getUser().equals(currentUser)).count()
        ).sum();
    }

    /**
     * returns list of suggested tags (other and main)
     * case-insensitiv
     *
     * @param tagName tag that starts with tagName
     * @return list of all tags that start with tagName
     */
    public static List<Tag> getSuggestedTags(String tagName) {
        return find().where().istartsWith(NAME_STR, tagName).findList();
    }

    /**
     * returns list of suggested tags which are main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of main tags that start with tagName
     */
    public static List<Tag> getSuggestedMainTags(String tagName) {
        List<Tag> list = find().where().eq(IS_MAIN_TAG_STR, true).istartsWith(NAME_STR, tagName).findList();
        return list.isEmpty() ? find().where().eq(IS_MAIN_TAG_STR, true).findList() : list;
    }

    /**
     * returns list of suggestet tags which are not main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of other tags that start with tagName
     */
    public static List<Tag> getSuggestedOtherTags(String tagName) {
        List<Tag> list = find().where().eq(IS_MAIN_TAG_STR, false).istartsWith(NAME_STR, tagName).findList();
        return list.isEmpty() ? find().where().eq(IS_MAIN_TAG_STR, false).findList() : list;
    }

    /**
     * returns the tag searched by name
     *
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().eq(NAME_STR, name).findUnique();
    }

    /**
     * gets main tag from db by name
     *
     * @param name the name
     * @return the maintag or null if it does not exist in db
     */
    public static Tag findMainTagByName(String name) {
        return find().where().eq(NAME_STR, name).eq(IS_MAIN_TAG_STR, true).findUnique();
    }

    /**
     * gets other tag from db by name
     *
     * @param name the name
     * @return the tag or null if it does not exist
     */
    public static Tag findOtherTagByName(String name) {
        return find().where().eq(NAME_STR, name).eq(IS_MAIN_TAG_STR, false).findUnique();
    }

    /**
     * get other tag or create if it does not exist
     *
     * @param tagName the tag name
     * @return the tag
     */
    public static Tag getOtherTagByNameOrCreate(String tagName) {
        Tag tag = findOtherTagByName(tagName);
        if (tag == null) {
            tag = TagBuilder.aTag().withName(tagName).build();
            create(tag);
        }
        return tag;
    }

}
