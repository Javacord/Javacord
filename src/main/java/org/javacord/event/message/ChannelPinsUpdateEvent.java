package org.javacord.event.message;

import org.javacord.entity.channel.TextChannel;
import org.javacord.event.Event;

import java.time.Instant;
import java.util.Optional;

/**
 * A channel pins update event.
 */
public class ChannelPinsUpdateEvent extends Event {

    /**
     * The channel of the event.
     */
    private final TextChannel channel;

    /**
     * The time at which the most recent pinned message was pinned.
     */
    private final Instant lastPinTimestamp;

    /**
     * Creates a new channel pins update event.
     *
     * @param channel The channel of the event.
     * @param lastPinTimestamp The time at which the most recent pinned message was pinned.
     */
    public ChannelPinsUpdateEvent(TextChannel channel, Instant lastPinTimestamp) {
        super(channel.getApi());
        this.channel = channel;
        this.lastPinTimestamp = lastPinTimestamp;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public TextChannel getChannel() {
        return channel;
    }

    /**
     * Gets the time at which the most recent pinned message was pinned.
     *
     * @return The time at which the most recent pinned message was pinned.
     */
    public Optional<Instant> getLastPinTimestamp() {
        return Optional.ofNullable(lastPinTimestamp);
    }

}
