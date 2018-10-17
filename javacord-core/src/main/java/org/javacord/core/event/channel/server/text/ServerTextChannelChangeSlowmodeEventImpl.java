package org.javacord.core.event.channel.server.text;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeSlowmodeEvent;

/**
 * The implementation for the ServerTextChannelChangeSlowmodeEvent.
 */
public class ServerTextChannelChangeSlowmodeEventImpl extends ServerTextChannelEventImpl
        implements ServerTextChannelChangeSlowmodeEvent {

    /**
     * The old delay of the channel.
     */
    private final int oldDelay;
    /**
     * The new delay of the channel.
     */
    private final int newDelay;

    /**
     * Creates a new server text channel change slowmode event.
     *
     * @param channel The channel of the event.
     * @param oldDelay The old delay of the channel.
     * @param newDelay The new delay of the channel.
     */
    public ServerTextChannelChangeSlowmodeEventImpl(ServerTextChannel channel, int oldDelay, int newDelay) {
        super(channel);
        this.oldDelay = oldDelay;
        this.newDelay = newDelay;
    }

    @Override
    public int getOldDelayInSeconds() {
        return oldDelay;
    }

    @Override
    public int getNewDelayInSeconds() {
        return newDelay;
    }
}
