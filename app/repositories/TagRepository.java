package repositories;

import com.avaje.ebean.Model;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Tag;
import models.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tourn on 4.4.16.
 */
@Singleton
public class TagRepository {
    private static final Model.Finder<Long, Tag> find = new Model.Finder<>(Tag.class);

    @Inject
    public TagRepository(){
        //noop
    }

    public List<Tag> getTrackedTags(User user){
        return user.getTrackings().stream().map(tracking -> tracking.getTag() ).collect(Collectors.toList());
    }

    public Model.Finder<Long, Tag>find(){
        return find;
    }
}
