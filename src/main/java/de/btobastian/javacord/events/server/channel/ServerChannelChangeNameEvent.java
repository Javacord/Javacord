package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
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
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param channel The channel of the event.
     */
    public ServerChannelChangeNameEvent(
            DiscordApi api, Server server, ServerChannel channel, String newName, String oldName) {
        super(api, server, channel);
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
