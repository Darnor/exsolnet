package controllers;

import javax.inject.Inject;

import models.Tag;
import play.libs.Json;
import play.mvc.Result;
import repositories.TagRepository;
import services.SessionService;
import views.html.tagList;

import java.util.List;
import java.util.stream.Collectors;

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

    public Result renderOverview() {
        return renderTagList(0, null);
    }

    /**
     *
     * @return
     */
    public Result renderTagList(int orderBy, String tagNameFilter) {
        if(!sessionService.isLoggedin()){
            return LoginController.redirectIfNotLoggedIn();
        }
        List<Tag> tags = tagRepository.find().all();
        if (tagNameFilter != null && tagNameFilter.length() > 0) {
            tags = tags.stream().filter(t -> t.getName().contains(tagNameFilter)).collect(Collectors.toList());
        }
        return ok(tagList.render(sessionService.getCurrentUserEmail(), tags, sessionService.getCurrentUser().getTrackings(), orderBy, tagNameFilter));
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
