package repositories;

import com.avaje.ebean.Model;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Exercise;
import models.Tag;
import models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static models.Tag.TagBuilder.aTag;

/**
 * Created by tourn on 4.4.16.
 */
@Singleton
public class TagRepository {
    private static final Model.Finder<Long, Tag> find = new Model.Finder<>(Tag.class);

    @Inject
    public TagRepository() {
        //noop
    }

    public void create(Tag tag) {
        tag.save();
    }

    public void update(Tag tag) {
        tag.update();
    }


    public List<Tag> getTrackedTags(User user) {
        return user.getTrackings().stream().map(tracking -> tracking.getTag()).collect(Collectors.toList());
    }

    public Model.Finder<Long, Tag> find() {
        return find;
    }

    public long getNofCompletedExercises(Tag tag, User currentUser) {
        return tag.getExercises().stream().mapToLong(exercise ->
                exercise.getSolutions().stream().filter(solution -> solution.getUser().equals(currentUser)).count()
        ).sum();
    }

    public List<Tag> getSuggestedTags(String query) {
        return this.find().where().startsWith("name", query).findList();
    }

    public Tag findTagByName(String name) {
        return this.find().where().eq("name", name).findUnique();
    }
}
