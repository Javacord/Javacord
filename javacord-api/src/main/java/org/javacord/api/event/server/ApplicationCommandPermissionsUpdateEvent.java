package org.javacord.api.event.server;

import org.javacord.api.interaction.ApplicationCommandPermissions;

import java.util.Optional;
import java.util.Set;

/**
 * An application command permissions update event.
 */
public interface ApplicationCommandPermissionsUpdateEvent extends ServerEvent {

    /**
     * Gets the ID of the application whose command permissions were updated.
     *
     * @return The ID of the application updated.
     */
    long getApplicationId();

    /**
     * Gets the ID of the command whose permissions were updated.
     * Returns an empty Optional if the permissions were updated for all commands.
     *
     * @return The ID of the command updated.
     */
    Optional<Long> getCommandId();

    /**
     * Gets the updated permissions.
     *
     * @return The updated permissions.
     */
    Set<ApplicationCommandPermissions> getUpdatedPermissions();
}
