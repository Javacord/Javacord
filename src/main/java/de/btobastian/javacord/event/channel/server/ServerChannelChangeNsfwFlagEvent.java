package de.btobastian.javacord.event.channel.server;

import de.btobastian.javacord.entity.channel.ServerChannel;

/**
 * A server channel change nsfw flag event.
 */
public class ServerChannelChangeNsfwFlagEvent extends ServerChannelEvent {

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
    public ServerChannelChangeNsfwFlagEvent(ServerChannel channel, boolean newNsfwFlag, boolean oldNsfwFlag) {
        super(channel);
        this.newNsfwFlag = newNsfwFlag;
        this.oldNsfwFlag = oldNsfwFlag;
    }

    /**
     * Gets the new nsfw flag of the channel.
     *
     * @return The new nsfw flag of the channel.
     */
    public boolean getNewNsfwFlag() {
        return newNsfwFlag;
    }

    /**
     * Gets the old nsfw flag of the channel.
     *
     * @return The old nsfw flag of the channel.
     */
    public boolean getOldNsfwFlag() {
        return oldNsfwFlag;
    }
}
