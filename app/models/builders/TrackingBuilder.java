package models.builders;

import models.Tag;
import models.Tracking;
import models.User;

/**
 * Created by tourn on 7.4.16.
 */
public class TrackingBuilder {
    private Tag tag;
    private User user;

    private TrackingBuilder() {
    }

    public static TrackingBuilder aTracking() {
        return new TrackingBuilder();
    }

    public TrackingBuilder withTag(Tag tag) {
        this.tag = tag;
        return this;
    }

    public TrackingBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public TrackingBuilder but() {
        return aTracking().withTag(tag).withUser(user);
    }

    public Tracking build() {
        return new Tracking(tag, user);
    }
}
