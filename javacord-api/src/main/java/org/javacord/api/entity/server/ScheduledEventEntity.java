package org.javacord.api.entity.server;

import org.javacord.api.entity.DiscordEntity;

import java.util.Optional;

public interface ScheduledEventEntity extends DiscordEntity {
    ScheduledEventType getType();

    Optional<ScheduledEventEntityMetadata> getDetails();
}
