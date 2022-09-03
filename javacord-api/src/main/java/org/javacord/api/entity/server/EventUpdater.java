package org.javacord.api.entity.server;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.server.internal.EventUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class EventUpdater {
    private final EventUpdaterDelegate delegate;

    public EventUpdater(ScheduledEvent event) {
        delegate = DelegateFactory.createEventUpdaterDelegate(event);
    }

    public EventUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }
    
    public EventUpdater setChannel(long channelId) {
        delegate.setChannel(channelId);
        return this;
    }
    
    public EventUpdater setChannel(Channel channel) {
        delegate.setChannel(channel);
        return this;
    }
    
    public EventUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }
    
    public EventUpdater setPrivacyLevel(PrivacyLevel privacyLevel) {
        delegate.setPrivacyLevel(privacyLevel);
        return this;
    }
    
    public EventUpdater setStartTime(Instant startTime) {
        delegate.setStartTime(startTime);
        return this;
    }
    
    public EventUpdater setEndTime(Instant endTime) {
        delegate.setEndTime(endTime);
        return this;
    }
    
    public EventUpdater setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }
    
    public EventUpdater setType(ScheduledEventType eventType) {
        delegate.setType(eventType);
        return this;
    }
    
    public EventUpdater setLocation(String location) {
        delegate.setLocation(location);
        return this;
    }
    
    public EventUpdater setStatus(ScheduledEventStatus status) {
        delegate.setStatus(status);
        return this;
    }

    EventUpdater setImage(BufferedImage image) {
        delegate.setImage(image);
        return this;
    }

    EventUpdater setImage(BufferedImage image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    EventUpdater setImage(File image) {
        delegate.setImage(image);
        return this;
    }

    EventUpdater setImage(URL image) {
        delegate.setImage(image);
        return this;
    }

    EventUpdater setImage(byte[] image) {
        delegate.setImage(image);
        return this;
    }

    EventUpdater setImage(byte[] image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    EventUpdater setImage(InputStream image) {
        delegate.setImage(image);
        return this;
    }

    EventUpdater setImage(InputStream image, String fileType) {
        delegate.setImage(image, fileType);
        return this;
    }

    CompletableFuture<ScheduledEvent> update() {
        return delegate.update();
    }
}
