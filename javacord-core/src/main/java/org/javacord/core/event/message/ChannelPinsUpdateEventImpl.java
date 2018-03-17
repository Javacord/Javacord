package org.javacord.core.event.message;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.ChannelPinsUpdateEvent;
import org.javacord.core.event.EventImpl;

import java.time.Instant;
import java.util.Optional;

/**
 * The implementation of {@link ChannelPinsUpdateEvent}.
 */
public class ChannelPinsUpdateEventImpl extends EventImpl implements ChannelPinsUpdateEvent {

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
    public ChannelPinsUpdateEventImpl(TextChannel channel, Instant lastPinTimestamp) {
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
