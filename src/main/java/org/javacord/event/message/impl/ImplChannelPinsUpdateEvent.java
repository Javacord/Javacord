package org.javacord.event.message.impl;

import org.javacord.entity.channel.TextChannel;
import org.javacord.event.impl.ImplEvent;
import org.javacord.event.message.ChannelPinsUpdateEvent;

import java.time.Instant;
import java.util.Optional;

/**
 * The implementation of {@link ChannelPinsUpdateEvent}.
 */
public class ImplChannelPinsUpdateEvent extends ImplEvent implements ChannelPinsUpdateEvent {

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
    public ImplChannelPinsUpdateEvent(TextChannel channel, Instant lastPinTimestamp) {
        super(channel.getApi());
        this.channel = channel;
        this.lastPinTimestamp = lastPinTimestamp;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public Optional<Instant> getLastPinTimestamp() {
        return Optional.ofNullable(lastPinTimestamp);
    }

}
