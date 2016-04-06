package controllers;

import javax.inject.Inject;

import models.Tag;
import play.libs.Json;
import play.mvc.Result;
import repositories.TagRepository;
import repositories.UserRepository;
import services.SessionService;
import views.html.tagList;

import static play.mvc.Results.ok;

/**
 * Created by revy on 05.04.16.
 */
public class TagController {

    @Inject
    TagRepository tagRepository;

    @Inject
    SessionService sessionService;

    /**
     *
     * @param tag
     * @return
     */
    public Result processTrack(Tag tag) {
        return ok();
    }

    /**
     *
     * @return
     */
    public Result renderTagList() {
        if(!sessionService.isLoggedin()){
            return LoginController.redirectIfNotLoggedIn();
        }
        return ok(tagList.render(sessionService.getCurrentUserEmail(), tagRepository.find().all(), sessionService.getCurrentUser().getTrackings()));
    }

    /**
     *
     * @param query
     * @return
     */
    public Result suggestTags(String query) {
        List<Tag> tagList = tagRepository.getSuggestedTags(query);
        return ok(Json.toJson(tagList));
    }
}
