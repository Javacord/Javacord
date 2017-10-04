package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerChannel;

/**
 * A server channel change name event.
 */
public class ServerChannelChangeNameEvent extends ServerChannelEvent {

    /**
     * The new name of the channel.
     */
    private final String newName;

    /**
     * The old name of the channel.
     */
    private final String oldName;

    /**
     * Creates a new server channel change name event.
     *
     * @param channel The channel of the event.
     * @param newName The new name of the channel.
     * @param oldName The old name of the channel.
     */
    public ServerChannelChangeNameEvent(ServerChannel channel, String newName, String oldName) {
        super(channel);
        this.newName = newName;
        this.oldName = oldName;
    }

    /**
     * Gets the new name of the channel.
     *
     * @return The new name of the channel.
     */
    public String getNewName() {
        return newName;
    }

    /**
     * Gets the old name of the channel.
     *
     * @return The old name of the channel.
     */
    public String getOldName() {
        return oldName;
    }
}
