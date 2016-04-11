package models;

import com.avaje.ebean.Model;

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
    private Long id;

    @Column(unique = true)
    @NotNull
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

    public void removeExercise(Long id) {
        exercises.removeIf(e -> e.getId() == id);
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

    /**
     * Creates new tag by name and maintag
     * @param name name of tag
     * @param isMainTag is maintag or not
     * @return new tag already created
     */
    public static Tag create(String name, Boolean isMainTag){
        Tag tag = new Tag(name, false);
        Tag.create(tag);
        return tag;
    }

    public static void update(Tag tag) {
        tag.update();
    }

    public static Model.Finder<Long, Tag> find() {
        return new Finder<Long, Tag>(Tag.class);
    }

    public long getNofCompletedExercises(User currentUser) {
        return getExercises().stream().mapToLong(exercise ->
                exercise.getSolutions().stream().filter(solution -> solution.getUser().equals(currentUser)).count()
        ).sum();
    }

    /**
     * returns list of suggested tags (other and main)
     *
     * @param tagName tag that starts with tagName
     * @return list of all tags that start with tagName
     */
    public static List<Tag> getSuggestedTags(String tagName) {
        return find().where().istartsWith("name", tagName).findList();
    }

    /**
     * Constructor for new Tag for one exercise
     *
     * @param name      the name of the tag
     * @param isMainTag is main tag or not
     */
    public Tag(String name, Boolean isMainTag) {
        this.name = name;
        this.setMainTag(isMainTag);
    }

    //default
    public Tag(){
        //do nothing
    }

    /**
     * returns list of suggested tags which are main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of main tags that start with tagName
     */
    public static List<Tag> getSuggestedMainTags(String tagName) {
        List<Tag> list = find().where().eq("isMainTag", true).istartsWith("name", tagName).findList();
        if (list.size() == 0)
            return find().where().eq("isMainTag", true).findList();

        return list;
    }

    /**
     * returns list of suggestet tags which are not main tags
     *
     * @param tagName tag that starts with tagName
     * @return list of other tags that start with tagName
     */
    public static List<Tag> getSuggestedOtherTags(String tagName) {

        List<Tag> list = find().where().eq("isMainTag", false).istartsWith("name", tagName).findList();
        if (list.size() == 0)
            return find().where().eq("isMainTag", false).findList();

        return list;
    }

    /**
     * returns the tag searched by name
     *
     * @param name the name of the tag, tag name sould be unique
     * @return the tag or null if it doesnt exist
     */
    public static Tag findTagByName(String name) {
        return find().where().eq("name", name).findUnique();
    }

    /**
     * gets main tag from db by name
     *
     * @param name the name
     * @return the maintag or null if it does not exist in db
     */
    public static Tag findMainTagByName(String name)    {
        return find().where().eq("name",name).eq("isMainTag",true).findUnique();
    }

    /**
     * gets other tag from db by name
     *
     * @param name the name
     * @return the tag or null if it does not exist
     */
    public static Tag findOtherTagByName(String name)    {
        return find().where().eq("name",name).eq("isMainTag",false).findUnique();
    }

    /**
     * get other tag or create if it does not exist
     * @param t the tag name
     * @return the tag
     */
    public static Tag getOtherTagByNameOrCreate(String t) {
        Tag tag = findOtherTagByName(t);
        return tag != null ? tag : create(t, false);
    }
}
