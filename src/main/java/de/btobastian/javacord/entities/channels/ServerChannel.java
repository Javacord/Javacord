package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.entities.Server;

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

}
