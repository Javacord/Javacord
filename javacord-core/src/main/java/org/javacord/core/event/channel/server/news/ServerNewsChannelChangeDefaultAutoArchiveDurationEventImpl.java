package org.javacord.core.event.channel.server.news;

import org.javacord.api.event.channel.server.news.ServerNewsChannelChangeDefaultAutoArchiveDurationEvent;
import org.javacord.core.entity.channel.ServerNewsChannelImpl;

/**
 * The implementation of {@link ServerNewsChannelChangeDefaultAutoArchiveDurationEvent}.
 */
public class ServerNewsChannelChangeDefaultAutoArchiveDurationEventImpl extends ServerNewsChannelEventImpl
        implements ServerNewsChannelChangeDefaultAutoArchiveDurationEvent {

    /**
     * The old default auto archive duration of the channel.
     */
    private final int oldDefaultAutoArchiveDuration;
    /**
     * The new default auto archive duration of the channel.
     */
    private final int newDefaultAutoArchiveDuration;

    /**
     * Creates a new server news channel change default auto archive duration event.
     *
     * @param channel                       The channel of the event.
     * @param newDefaultAutoArchiveDuration The new default auto archive duration of the channel.
     * @param oldDefaultAutoArchiveDuration The old default auto archive duration of the channel.
     */
    public ServerNewsChannelChangeDefaultAutoArchiveDurationEventImpl(
            ServerNewsChannelImpl channel, final int oldDefaultAutoArchiveDuration,
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
