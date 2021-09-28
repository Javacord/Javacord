package org.javacord.core.event.channel.server.text;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeDefaultAutoArchiveDurationEvent;

/**
 * The implementation for the ServerTextChannelChangeDefaultAutoArchiveDurationEvent.
 */
public class ServerTextChannelChangeDefaultAutoArchiveDurationEventImpl extends ServerTextChannelEventImpl
        implements ServerTextChannelChangeDefaultAutoArchiveDurationEvent {

    /**
     * The old default auto archive duration of the channel.
     */
    private final int oldDefaultAutoArchiveDuration;
    /**
     * The new default auto archive duration of the channel.
     */
    private final int newDefaultAutoArchiveDuration;

    /**
     * Creates a new server text channel change default auto archive duration event.
     *
     * @param channel                       The channel of the event.
     * @param oldDefaultAutoArchiveDuration The old default auto archive duration of the channel.
     * @param newDefaultAutoArchiveDuration The new default auto archive duration of the channel.
     */
    public ServerTextChannelChangeDefaultAutoArchiveDurationEventImpl(final ServerTextChannel channel,
                                                                      final int oldDefaultAutoArchiveDuration,
                                                                      final int newDefaultAutoArchiveDuration) {
        super(channel);
        this.oldDefaultAutoArchiveDuration = oldDefaultAutoArchiveDuration;
        this.newDefaultAutoArchiveDuration = newDefaultAutoArchiveDuration;
    }

    @Override
    public int getOldDefaultAutoArchiveDuration() {
        return oldDefaultAutoArchiveDuration;
    }

    @Override
    public int getAutoArchiveDuration() {
        return newDefaultAutoArchiveDuration;
    }
}
