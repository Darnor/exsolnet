package controllers;

import javax.inject.Inject;

import models.Tag;
import play.libs.Json;
import play.mvc.Result;
import services.SessionService;
import views.html.tagList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * Created by revy on 05.04.16.
 */
public class TagController {

    @Inject
    SessionService sessionService;

    public Result processTrack(Tag tag) {
        return ok();
    }

    public Result renderOverview() {
        return renderTagList(1, null);
    }

    /**
     * @param tags list containing tags
     * @param tagNameFilter string to filter tags names by
     * @return filterted tag list
     */
    private List<Tag> filterTagList(List<Tag> tags, String tagNameFilter) {
        if (tagNameFilter != null && tagNameFilter.length() > 0) {
            tags = tags.stream().filter(t -> t.getName().toLowerCase().contains(tagNameFilter.toLowerCase())).collect(Collectors.toList());
        }
        return tags;
    }

    /**
     * @param tags List containing tags
     * @param trackedTags List containing the trackings for the current user
     * @param orderBy sort column
     * @return the sorted List depending on orderBy
     */
    private List<Tag> sortTagList(List<Tag> tags, List<Tag> trackedTags, int orderBy) {
        tags.sort((t1, t2) -> {
            switch(Math.abs(orderBy)) {
               case 1:
                    return t1.getName().toLowerCase().compareTo(t2.getName().toLowerCase());
               case 2:
                    int t1ExSize = t1.getExercises().size();
                    int t2ExSize = t2.getExercises().size();
                    return t1ExSize > t2ExSize ? -1 : t1ExSize < t2ExSize ? 1 : 0;
               default:
                    return trackedTags.contains(t1) ? -1 : trackedTags.contains(t2) ? 1 : 0;
            }
        });

        if (orderBy < 0) {
            Collections.reverse(tags);
        }

        return tags;
    }

    /**
     * @param orderBy colum to order the tags
     * @param tagNameFilter filter for the tags
     * @return renders the tags site
     */
    public Result renderTagList(int orderBy, String tagNameFilter) {
        if(!sessionService.isLoggedin()){
            return LoginController.redirectIfNotLoggedIn();
        }
        List<Tag> trackedTags = sessionService.getCurrentUser().getTrackedTags();
        List<Tag> tags = sortTagList(filterTagList(Tag.find().all(), tagNameFilter), trackedTags, orderBy);
        return ok(tagList.render(sessionService.getCurrentUserEmail(), tags, trackedTags, orderBy, tagNameFilter));
    }

    /**
     *
     * @param query
     * @return
     */
    public Result suggestTags(String query) {
        List<Tag> tagList = Tag.getSuggestedTags(query);
        return suggestTagsByList(tagList);
    }

    /**
     * suggests main tags
     * @param tagName suggest tags for tagName
     * @return Result -> list of main tags
     */
    public Result suggestMainTags(String tagName) {
        List<Tag> tagList = Tag.getSuggestedMainTags(tagName);
        return suggestTagsByList(tagList);
    }

    /**
     * suggest non main tags
     * @param tagName suggest tags for tagName
     * @return Result -> list of non main tags
     */
    public Result suggestOtherTags(String tagName) {
        List<Tag> tagList = Tag.getSuggestedOtherTags(tagName);
        return suggestTagsByList(tagList);
    }
    /**
     * return json list with suggested tags with given list
     * @param tagList the list with suggested tags
     * @return result of tags
     */
    public Result suggestTagsByList(List<Tag> tagList){
        List<TagEntry> list = new ArrayList<TagEntry>();
        for(Tag tag : tagList){
            list.add(new TagEntry(tag.getName()));
        }
        return ok(Json.toJson(list));
    }
    public static class TagEntry{
        public final String name;
        public TagEntry(String name) {
            this.name = name;
        }
    }
}
