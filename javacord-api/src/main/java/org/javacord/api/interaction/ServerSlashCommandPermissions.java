package org.javacord.api.interaction;

import java.util.List;

public interface ServerSlashCommandPermissions {
    /**
     * Gets the ID of this server slash command permissions.
     *
     * @return The id.
     */
    long getId();

    /**
     * Gets the application ID from this server slash command permissions.
     *
     * @return The application id.
     */
    long getApplicationId();

    /**
     * Gets the server ID of this server slash command permissions.
     *
     * @return The server ID.
     */
    long getServerId();

    /**
     * Gets the permissions of this server slash command permissions.
     *
     * @return A list containing the slash command permissions.
     */
    List<SlashCommandPermissions> getPermissions();
}
