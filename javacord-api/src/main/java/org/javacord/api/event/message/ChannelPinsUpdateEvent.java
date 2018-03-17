package org.javacord.api.event.message;

import org.javacord.api.event.channel.TextChannelEvent;

import java.time.Instant;
import java.util.Optional;

/**
 * A channel pins update event.
 */
public interface ChannelPinsUpdateEvent extends TextChannelEvent {

    /**
     * Gets the time at which the most recent pinned message was pinned.
     *
     * @return The time at which the most recent pinned message was pinned.
     */
    Optional<Instant> getLastPinTimestamp();

}
