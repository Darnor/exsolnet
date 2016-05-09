package models;

import com.avaje.ebean.Model;
import models.builders.TagBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static models.Tracking.COLUMN_TAG_ID;

@Entity
@Table(name = "tag")
public class Tag extends Model {

    public enum Type {
        MAIN(true), NORMAL(false);

        boolean value;

        Type(boolean value) {
            this.value = value;
        }
    }

    private static final String COLUMN_IS_MAIN_TAG = "isMainTag";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TAGS = "tags";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = COLUMN_NAME, unique = true)
    @NotNull
    private String name;

    @Column(name = COLUMN_IS_MAIN_TAG)
    @NotNull
    private boolean isMainTag;

    @ManyToMany(mappedBy = COLUMN_TAGS)
    private List<Exercise> exercises;

    @OneToMany
    @JoinColumn(name = COLUMN_TAG_ID)
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

    public static Tag create(String tagName, Type type) {
        Tag tag = TagBuilder.aTag().withName(tagName).withIsMainTag(type.value).build();
        tag.save();
        return tag;
    }

    public static List<Tag> getFilteredTags(String tagNameFilter) {
        return find().where().icontains(COLUMN_NAME, tagNameFilter).orderBy(COLUMN_NAME).findList();
    }

    /**
     * returns list of suggested tags (other and main)
     * case-insensitiv
     *
     * @param tagName tag that starts with tagName
     * @return list of all tags that start with tagName
     */
    public static List<Tag> getSuggestedTags(String tagName) {
        return find().where().istartsWith(COLUMN_NAME, tagName).orderBy(COLUMN_NAME).findList();
    }

    /**
     * returns the tag searched by name
     *
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().ieq(COLUMN_NAME, name).findUnique();
    }

    public static Tag findById(Long id) {
        return find().byId(id);
    }

    /**
     *
     * @return all tags based in their types
     */
    public static List<Tag> findTagsByType(Type type){
        return find().where().eq(COLUMN_IS_MAIN_TAG, type.value).orderBy(COLUMN_NAME).findList();
    }

    public static List<Tag> createTagList(Type type, String... tagNames) {
        return Arrays.stream(tagNames)
                .map(String::trim)
                .filter(t -> t.length() > 0)
                .distinct()
                .map(s -> {
                    Tag tag = findTagByName(s);
                    return tag == null ? create(s, type) : tag;
                })
                .collect(Collectors.toList());
    }
}
