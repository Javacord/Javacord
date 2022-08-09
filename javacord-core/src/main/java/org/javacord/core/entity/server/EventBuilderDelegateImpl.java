package org.javacord.core.entity.server;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.server.PrivacyLevel;
import org.javacord.api.entity.server.ScheduledEventType;
import org.javacord.api.entity.server.internal.EventBuilderDelegate;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class EventBuilderDelegateImpl implements EventBuilderDelegate {
    private final ServerImpl server;

    public EventBuilderDelegateImpl(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void setChannel(long channelId) {
        
    }

    @Override
    public void setChannel(Channel channel) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setPrivacyLevel(PrivacyLevel privacyLevel) {

    }

    @Override
    public void setStartTime(Instant startTime) {

    }

    @Override
    public void setEndTime(Instant endTime) {

    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void setType(ScheduledEventType eventType) {

    }

    @Override
    public void setLocation(String location) {

    }

    @Override
    public void setImage(BufferedImage image) {

    }

    @Override
    public void setImage(BufferedImage image, String fileType) {

    }

    @Override
    public void setImage(File image) {

    }

    @Override
    public void setImage(URL image) {

    }

    @Override
    public void setImage(byte[] image) {

    }

    @Override
    public void setImage(byte[] image, String fileType) {

    }

    @Override
    public void setImage(InputStream image) {

    }

    @Override
    public void setImage(InputStream image, String fileType) {

    }

    @Override
    public CompletableFuture<Long> create() {
        return null;
    }
}
