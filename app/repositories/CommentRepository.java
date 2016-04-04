package repositories;

import com.avaje.ebean.Model;
import models.Comment;
import models.User;

import java.util.List;

/**
 * Created by tourn on 4.4.16.
 */
public class CommentRepository {

    private static final Model.Finder<Long, Comment> find = new Model.Finder<>(Comment.class);

    private static final int NOF_RECENT_COMMENTS = 5;

    public List<Comment> getRecentComments(User user) {
        return find().where()
                .eq("user", user)
                .orderBy("time desc")
                .setMaxRows(NOF_RECENT_COMMENTS)
                .findList();
    }

    public Model.Finder<Long, Comment> find(){
        return find;
    }
}
