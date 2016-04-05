package controllers;

import javax.inject.Inject;

import models.Tag;
import play.mvc.Result;
import repositories.TagRepository;
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

    public Result processCreate(String name) {
        return ok(tagList.render(sessionService.getUsername(), tagRepository.find().all()));
    }

    public Result processTrack(Tag tag) {
        return ok(tagList.render(sessionService.getUsername(), tagRepository.find().all()));
    }

    public Result renderTagList() {
        return ok(tagList.render(sessionService.getUsername(), tagRepository.find().all()));
    }

    public Result suggestTags(String query) {
        return ok(tagList.render(sessionService.getUsername(), tagRepository.find().all()));
    }
}
