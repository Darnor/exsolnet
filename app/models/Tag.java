package models;

import com.avaje.ebean.Model;
import models.builders.TagBuilder;

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
    private long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @NotNull
    private boolean isMainTag;

    @ManyToMany(mappedBy = "tags")
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = "tag")
    private List<Tracking> trackings;

    private static final String IS_MAIN_TAG = "isMainTag";
    private static final String TAG_NAME = "name";

    /**
     * Constructor for new Tag for one exercise
     *
     * @param name      the name of the tag
     * @param isMainTag is main tag or not
     */
    public Tag(String name, boolean isMainTag) {
        this.id = 0;
        this.name = name;
        this.isMainTag = isMainTag;
    }

    public long getId() {
        return id;
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
        return exercises;
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    /**
     * Save tag in db
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
        return find().where().istartsWith(TAG_NAME, tagName).findList();
    }

    /**
     * returns list of suggested tags which are main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of main tags that start with tagName
     */
    public static List<Tag> getSuggestedMainTags(String tagName) {
        List<Tag> list = find().where().eq(IS_MAIN_TAG, true).istartsWith(TAG_NAME, tagName).findList();
        return list.isEmpty() ? find().where().eq(IS_MAIN_TAG, true).findList() : list;
    }

    /**
     * returns list of suggestet tags which are not main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of other tags that start with tagName
     */
    public static List<Tag> getSuggestedOtherTags(String tagName) {
        List<Tag> list = find().where().eq(IS_MAIN_TAG, false).istartsWith(TAG_NAME, tagName).findList();
        return list.isEmpty() ? find().where().eq(IS_MAIN_TAG, false).findList() : list;
    }

    /**
     * returns the tag searched by name
     *
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().eq(TAG_NAME, name).findUnique();
    }

    /**
     * gets main tag from db by name
     *
     * @param name the name
     * @return the maintag or null if it does not exist in db
     */
    public static Tag findMainTagByName(String name)    {
        return find().where().eq(TAG_NAME, name).eq(IS_MAIN_TAG, true).findUnique();
    }

    /**
     * gets other tag from db by name
     *
     * @param name the name
     * @return the tag or null if it does not exist
     */
    public static Tag findOtherTagByName(String name)    {
        return find().where().eq(TAG_NAME, name).eq(IS_MAIN_TAG, false).findUnique();
    }

    /**
     * get other tag or create if it does not exist
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tag tag = (Tag) o;

        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
