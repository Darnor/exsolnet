package models;

import com.avaje.ebean.Model;
import models.builders.TagBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "tag")
public class Tag extends Model {

    private static final String COLUMN_IS_MAIN_TAG = "isMainTag";
    private static final String COLUMN_TAG_NAME = "name";
    private static final String COLUMN_TAGS = "tags";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = COLUMN_TAG_NAME, unique = true)
    @NotNull
    private String name;

    @Column(name = COLUMN_IS_MAIN_TAG)
    @NotNull
    private boolean isMainTag;

    @ManyToMany(mappedBy = COLUMN_TAGS)
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = Tracking.COLUMN_TAG_ID)
    private List<Tracking> trackings;

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

    public static Tag create(String tagName, boolean isMainTag) {
        Tag tag = TagBuilder.aTag().withName(tagName).withIsMainTag(isMainTag).build();
        tag.save();
        return tag;
    }

    public static List<Tag> getFilteredTags(String tagNameFilter) {
        return find().where().icontains(COLUMN_TAG_NAME, tagNameFilter).findList();
    }

    /**
     * returns list of suggested tags (other and main)
     * case-insensitiv
     *
     * @param tagName tag that starts with tagName
     * @return list of all tags that start with tagName
     */
    public static List<Tag> getSuggestedTags(String tagName) {
        return find().where().istartsWith(COLUMN_TAG_NAME, tagName).orderBy(COLUMN_TAG_NAME).findList();
    }

    /**
     * returns the tag searched by name
     *
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().ieq(COLUMN_TAG_NAME, name).findUnique();
    }

    /**
     *
     * @return all main tags
     */
    public static List<Tag> getAllMain(){
        List<Tag> list = find().where().eq(COLUMN_IS_MAIN_TAG,true).findList();
        return list;
    }

    /**
     *
     * @return all other tags
     */
    public static List<Tag> getAllOther(){
        List<Tag> list = find().where().eq(COLUMN_IS_MAIN_TAG,false).findList();
        return list;
    }
}
