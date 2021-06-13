package org.javacord.api.interaction;

import java.util.List;

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
     * Gets the permissions of this server application command permissions.
     *
     * @return A list containing the application command permissions.
     */
    List<ApplicationCommandPermissions> getPermissions();
}
