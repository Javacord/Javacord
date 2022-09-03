package org.javacord.api.entity.server;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.time.Instant;
import java.util.Optional;

public interface ScheduledEvent extends DiscordEntity, Nameable {
    // TODO: add javadoc

    Long getServerId();

    Optional<Long> getChannelId();

    Optional<User> getCreator();

    Optional<Long> getCreatorId();

    Optional<Long> getEntityId();

    Instant getStartTime();

    Optional<Instant> getEndTime();

    PrivacyLevel getPrivacyLevel();

    ScheduledEventStatus getStatus();

    Optional<String> getDescription();

    ScheduledEventType getType();

    Optional<String> getLocation();

    Optional<Integer> getUserCount();

    Optional<Icon> getCoverImage();

    Optional<String> getCoverImageHash();

    EventUpdater createUpdater();
}
