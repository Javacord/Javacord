package org.javacord.api.event.channel.server;

/**
 * A server channel change name event.
 */
public interface ServerChannelChangeNameEvent extends ServerChannelEvent {

    /**
     * Gets the new name of the channel.
     *
     * @return The new name of the channel.
     */
    String getNewName();

    /**
     * Gets the old name of the channel.
     *
     * @return The old name of the channel.
     */
    String getOldName();

}
