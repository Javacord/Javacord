package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;

import java.util.List;

/**
 * This class represents a server channel.
 */
public interface ServerChannel extends Channel {

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    String getName();

    /**
     * Gets the server of the channel.
     *
     * @return The server of the channel.
     */
    Server getServer();

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     */
    void addServerChannelDeleteListener(ServerChannelDeleteListener listener);

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    List<ServerChannelDeleteListener> getServerChannelDeleteListeners();

}
