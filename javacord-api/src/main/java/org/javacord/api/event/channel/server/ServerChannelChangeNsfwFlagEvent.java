package org.javacord.api.event.channel.server;

/**
 * A server channel change nsfw flag event.
 */
public interface ServerChannelChangeNsfwFlagEvent extends ServerChannelEvent {

    /**
     * Gets the new nsfw flag of the channel.
     *
     * @return The new nsfw flag of the channel.
     */
    boolean getNewNsfwFlag();

    /**
     * Gets the old nsfw flag of the channel.
     *
     * @return The old nsfw flag of the channel.
     */
    boolean getOldNsfwFlag();

}
