package org.javacord.core.event.channel.server;

import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.channel.server.ServerChannelChangeNsfwFlagEvent;

/**
 * The implementation of {@link ServerChannelChangeNsfwFlagEvent}.
 */
public class ServerChannelChangeNsfwFlagEventImpl extends ServerChannelEventImpl
        implements ServerChannelChangeNsfwFlagEvent {

    /**
     * The new name of the channel.
     */
    private final boolean newNsfwFlag;

    /**
     * The old name of the channel.
     */
    private final boolean oldNsfwFlag;

    /**
     * Creates a new server channel change nsfw flag event.
     *
     * @param channel The channel of the event.
     * @param newNsfwFlag The new nsfw flag of the channel.
     * @param oldNsfwFlag The old nsfw flag of the channel.
     */
    public ServerChannelChangeNsfwFlagEventImpl(ServerChannel channel, boolean newNsfwFlag, boolean oldNsfwFlag) {
        super(channel);
        this.newNsfwFlag = newNsfwFlag;
        this.oldNsfwFlag = oldNsfwFlag;
    }

    @Override
    public boolean getNewNsfwFlag() {
        return newNsfwFlag;
    }

    @Override
    public boolean getOldNsfwFlag() {
        return oldNsfwFlag;
    }

}
