package org.javacord.api.entity.server;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.server.internal.EventBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class EventBuilder {
    private final EventBuilderDelegate delegate;

    public EventBuilder(Server server) {
        delegate = DelegateFactory.createEventBuilderDelegate(server);
    }

    public EventBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    public EventBuilder setChannel(long channelId) {
        delegate.setChannel(channelId);
        return this;
    }

    public EventBuilder setChannel(Channel channel) {
        delegate.setChannel(channel);
        return this;
    }

    public EventBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    public EventBuilder setPrivacyLevel(PrivacyLevel privacyLevel) {
        delegate.setPrivacyLevel(privacyLevel);
        return this;
    }

    public EventBuilder setStartTime(Instant startTime) {
        delegate.setStartTime(startTime);
        return this;
    }

    public EventBuilder setEndTime(Instant endTime) {
        delegate.setEndTime(endTime);
        return this;
    }

    public EventBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    public EventBuilder setType(ScheduledEventType eventType) {
        delegate.setType(eventType);
        return this;
    }

    public EventBuilder setLocation(String location) {
        delegate.setLocation(location);
        return this;
    }

    public EventBuilder setImage(BufferedImage image) {
        delegate.setImage(image);
        return this;
    }

    public EventBuilder setImage(BufferedImage image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    public EventBuilder setImage(File image) {
        delegate.setImage(image);
        return this;
    }

    public EventBuilder setImage(URL image) {
        delegate.setImage(image);
        return this;
    }

    public EventBuilder setImage(byte[] image) {
        delegate.setImage(image);
        return this;
    }

    public EventBuilder setImage(byte[] image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    public EventBuilder setImage(InputStream image) {
        delegate.setImage(image);
        return this;
    }

    public EventBuilder setImage(InputStream image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    public CompletableFuture<ScheduledEvent> create() {
        return delegate.create();
    }
}
