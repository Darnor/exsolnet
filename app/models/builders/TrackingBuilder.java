package models.builders;

import models.Tag;
import models.Tracking;
import models.User;

public class TrackingBuilder {
    private Long id;
    private Tag tag;
    private User user;

    private TrackingBuilder() {
    }

    public static TrackingBuilder aTracking() {
        return new TrackingBuilder();
    }

    public TrackingBuilder withId(Long id) {
        this.id = id;
        return this;
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
        return aTracking().withId(id).withTag(tag).withUser(user);
    }

    public Tracking build() {
        Tracking tracking = new Tracking();
        tracking.setId(id);
        tracking.setTag(tag);
        tracking.setUser(user);
        return tracking;
    }
}
