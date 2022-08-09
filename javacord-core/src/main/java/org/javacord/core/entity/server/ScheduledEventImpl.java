package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.PrivacyLevel;
import org.javacord.api.entity.server.ScheduledEvent;
import org.javacord.api.entity.server.ScheduledEventEntity;
import org.javacord.api.entity.server.ScheduledEventStatus;
import org.javacord.api.entity.user.User;

import java.time.Instant;
import java.util.Optional;

public class ScheduledEventImpl implements ScheduledEvent {
    public ScheduledEventImpl(Server server, JsonNode jsonBody) {
    }

    @Override
    public DiscordApi getApi() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public Optional<Channel> getChannel() {
        return Optional.empty();
    }

    @Override
    public User getCreator() {
        return null;
    }

    @Override
    public Instant getStartTime() {
        return null;
    }

    @Override
    public Optional<Instant> getEndTime() {
        return Optional.empty();
    }

    @Override
    public PrivacyLevel getPrivacyLevel() {
        return null;
    }

    @Override
    public ScheduledEventStatus getStatus() {
        return null;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.empty();
    }

    @Override
    public ScheduledEventEntity getEventEntity() {
        return null;
    }

    @Override
    public Optional<Integer> getUserCount() {
        return Optional.empty();
    }
}
