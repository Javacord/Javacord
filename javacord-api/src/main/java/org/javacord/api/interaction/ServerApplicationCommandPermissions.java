package org.javacord.api.interaction;

import org.javacord.api.entity.server.Server;

import java.util.Set;

public interface ServerApplicationCommandPermissions {
    /**
     * Gets the ID of this server application command permissions.
     *
     * @return The id.
     */
    long getId();

    /**
     * Gets the application ID from this server application command permissions.
     *
     * @return The application id.
     */
    long getApplicationId();

    /**
     * Gets the server ID of this server application command permissions.
     *
     * @return The server ID.
     */
    long getServerId();

    /**
     * Gets the server of this server application command permissions.
     *
     * @return The server.
     */
    Server getServer();

    /**
     * Gets the permissions of this server application command permissions.
     *
     * @return The application command permissions.
     */
    Set<ApplicationCommandPermissions> getPermissions();
}
