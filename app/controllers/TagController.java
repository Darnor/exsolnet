package controllers;

import models.Tag;
import models.Tracking;
import models.User;
import models.builders.TrackingBuilder;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;
import views.html.error404;
import views.html.tagList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Security.Authenticated(Secured.class)
public class TagController extends Controller {

    private static final String TAG_FILTER = "tagFilter";
    private static final String TAG_ORDER = "tagOrder";

    /**
     * return a sorted list depending on the orderBy value
     * per default the list is sorted by the first column aka the tag name
     *
     * @param tags        List containing tags
     * @param trackedTags List containing the trackings for the current user
     * @param orderBy     sort column
     * @return the sorted List depending on orderBy
     */
    private static List<Tag> sortTagList(List<Tag> tags, List<Tag> trackedTags, int orderBy) {
        tags.sort((t1, t2) -> {
            switch (Math.abs(orderBy)) {
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
     * @param tagId Long containing the Tag Id which needs to be tracked or if tracked untracked
     * @return renders the tagList again
     */
    public Result processTrack(long tagId) {
        User currentUser = SessionService.getCurrentUser();
        Tag tag = Tag.findById(tagId);

        if (tag == null) {
            Logger.error("Tag to track was not found.");
            return notFound(error404.render(currentUser, "Das zu trackende Tag wurde nicht gefunden."));
        }

        Tracking tracking = currentUser.getTrackings().stream().filter(t -> t.getTag().getId().equals(tag.getId())).findFirst().orElse(null);

        if (tracking == null) {
            TrackingBuilder.aTracking().withTag(tag).withUser(currentUser).build().save();
        } else {
            tracking.delete();
        }

        return redirect(routes.TagController.renderTagList(Integer.parseInt(session(TAG_ORDER)), session(TAG_FILTER)));
    }

    /**
     * Render the overview of the tag list
     * @return Result
     */
    public Result renderOverview() {
        return renderTagList(1, "");
    }

    /**
     * @param orderBy       colum to order the tags
     * @param tagNameFilter filter for the tags
     * @return renders the tags site
     */
    public Result renderTagList(int orderBy, String tagNameFilter) {
        User currentUser = SessionService.getCurrentUser();
        List<Tag> trackedTags = currentUser.getTrackedTags();
        List<Tag> tags = sortTagList(Tag.getFilteredTags(tagNameFilter), trackedTags, orderBy);
        session(TAG_FILTER, tagNameFilter);
        session(TAG_ORDER, Integer.toString(orderBy));
        return ok(tagList.render(currentUser, tags, trackedTags, orderBy, tagNameFilter));
    }

    /**
     * Parse TagName for longname and name to json.
     * @param tagName the name od the tag
     * @return Result
     */
    public Result processTagNameQuery(String tagName) {
        return ok(Json.toJson(Tag.getSuggestedTags(tagName).stream().map(t -> new TagEntry(t.getName(), t.getLongName())).collect(Collectors.toList())));
    }

    /**
     * A TagEntry containing Long name and name for namequery
     */
    private class TagEntry {
        public final String name;
        public final String longname;

        TagEntry(String name, String longname) {
            this.name = name;
            this.longname = longname;
        }
    }
}
