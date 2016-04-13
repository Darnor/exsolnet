package controllers;

import models.Tag;
import models.Tracking;
import models.User;
import models.builders.TrackingBuilder;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.tagList;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static play.mvc.Controller.session;
import static play.mvc.Results.ok;

/**
 * Created by revy on 05.04.16.
 */
@Security.Authenticated(Secured.class)

public class TagController {

    private static final String TAG_FILTER = "tagFilter";
    private static final String TAG_ORDER = "tagOrder";

    private void createTrack(User user, Tracking tracking) {
        try {
            user.addTracking(tracking);
            tracking.save();
        } catch (OptimisticLockException ex) {
            user.removeTracking(tracking.getId());
            throw ex;
        }
    }

    private void updateTrackingStatus(Tracking tracking) {
        if (tracking.getTrackingStatus()) {
            tracking.unTrack();
        } else {
            tracking.track();
        }
    }

    private void updateTrack(Tracking tracking) {
        try {
            updateTrackingStatus(tracking);
            tracking.update();
        } catch (OptimisticLockException ex) {
            updateTrackingStatus(tracking);
            throw ex;
        }
    }

    /**
     * @param tagName String containing the Tag Name which needs to be tracked or if tracked untracked
     * @return renders the tagList again
     */
    public Result processTrack(String tagName) {
        User currentUser = SessionService.getCurrentUser();
        Tag tag = Tag.findTagByName(tagName);
        Tracking tracking = currentUser.getTrackingByTag(tag);
        if (tracking == null || !tracking.getTrackingStatus()) {
            createTrack(currentUser, TrackingBuilder.aTracking().withTag(tag).withUser(currentUser).build());
        } else {
            updateTrack(tracking);
        }
        return renderTagList(Integer.parseInt(session(TAG_ORDER)), session(TAG_FILTER));
    }

    public Result renderOverview() {
        return renderTagList(1, "");
    }

    /**
     * @param tags list containing tags
     * @param tagNameFilter string to filter tags names by
     * @return filterted tag list
     */
    private List<Tag> filterTagList(List<Tag> tags, String tagNameFilter) {
        List<Tag> filteredTags = tags;
        if (tagNameFilter != null && tagNameFilter.length() > 0) {
            filteredTags = tags.stream().filter(t -> t.getName().toLowerCase().contains(tagNameFilter.toLowerCase())).collect(Collectors.toList());
        }
        return filteredTags;
    }

    /**
     * return a sorted list depending on the orderBy value
     * per default the list is sorted by the first column aka the tag name
     *
     * @param tags List containing tags
     * @param trackedTags List containing the trackings for the current user
     * @param orderBy sort column
     * @return the sorted List depending on orderBy
     */
    private List<Tag> sortTagList(List<Tag> tags, List<Tag> trackedTags, int orderBy) {
        tags.sort((t1, t2) -> {
            switch(Math.abs(orderBy)) {
                case 2:
                    int t1ExSize = t1.getExercises().size();
                    int t2ExSize = t2.getExercises().size();
                    return t2ExSize - t1ExSize;
                case 3:
                    return trackedTags.contains(t1) ? -1 : trackedTags.contains(t2) ? 1 : 0;
                default:
                    return t1.getName().compareToIgnoreCase(t2.getName());
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
        List<Tag> trackedTags = SessionService.getCurrentUser().getTrackedTags();
        List<Tag> tags = sortTagList(filterTagList(Tag.find().all(), tagNameFilter), trackedTags, orderBy);
        session(TAG_FILTER, tagNameFilter);
        session(TAG_ORDER, Integer.toString(orderBy));
        return ok(tagList.render(SessionService.getCurrentUserEmail(), tags, trackedTags, orderBy, tagNameFilter));
    }

    /**
     * suggests all tags (main and other) which starts with the tagName.
     * @param tagName suggest tags for tagName
     * @return Result -> list of all T
     */
    public Result suggestTags(String tagName) {
        List<Tag> tagList = Tag.getSuggestedTags(tagName);
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
        Tag t = new Tag(tagName,false);
        tagList.add(0,t);
        return suggestTagsByList(tagList);

    }
    /**
     * return json list with suggested tags with given list
     * @param tagList the list with suggested tags
     * @return result of tags
     */
    public Result suggestTagsByList(List<Tag> tagList){
        List<TagEntry> list = new ArrayList<>();
        for(Tag tag : tagList){
            list.add(new TagEntry(tag.getName()));
        }
        return ok(Json.toJson(list));
    }
    public class TagEntry{
        public final String name;
        public TagEntry(String name) {
            this.name = name;
        }
    }
}
