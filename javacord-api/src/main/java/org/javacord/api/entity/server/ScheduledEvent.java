package org.javacord.api.entity.server;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.user.User;

import java.time.Instant;
import java.util.Optional;

public interface ScheduledEvent extends DiscordEntity, Nameable {
    // TODO: add javadoc

    Server getServer();

    Optional<Channel> getChannel();

    User getCreator();

    Instant getStartTime();

    Optional<Instant> getEndTime();

    PrivacyLevel getPrivacyLevel();

    ScheduledEventStatus getStatus();

    Optional<String> getDescription();

    ScheduledEventEntity getEventEntity();

    Optional<Integer> getUserCount();

    // TODO: Optional<Image> getCoverImage();
}
