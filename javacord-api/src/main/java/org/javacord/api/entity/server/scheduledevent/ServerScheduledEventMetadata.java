package org.javacord.api.entity.server.scheduledevent;

import java.util.Optional;

public interface ServerScheduledEventMetadata {

    /**
     * Gets the location of the event.
     * Always present for {@link ServerScheduledEventType#EXTERNAL} events.
     *
     * @return The location of the event.
     */
    Optional<String> getLocation();

}
