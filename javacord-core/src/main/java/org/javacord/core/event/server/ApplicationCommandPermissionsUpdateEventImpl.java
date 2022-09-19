package org.javacord.core.event.server;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ApplicationCommandPermissionsUpdateEvent;
import org.javacord.api.interaction.ApplicationCommandPermissions;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * The implementation of {@link ApplicationCommandPermissionsUpdateEvent}.
 */
public class ApplicationCommandPermissionsUpdateEventImpl extends ServerEventImpl
        implements ApplicationCommandPermissionsUpdateEvent {

    private final long applicationId;
    private final long commandId;
    private final Set<ApplicationCommandPermissions> newPermissions;

    /**
     * Creates a new application command permissions update event.
     *
     * @param applicationId The ID of the application being updated.
     * @param commandId The ID of the command being updated.
     * @param server The server this event is for.
     * @param newPermissions The new permissions set.
     */
    public ApplicationCommandPermissionsUpdateEventImpl(long applicationId, long commandId,
                                                        Server server,
                                                        Set<ApplicationCommandPermissions> newPermissions) {
        super(server);
        this.applicationId = applicationId;
        this.commandId = commandId;
        this.newPermissions = newPermissions;
    }

    @Override
    public long getApplicationId() {
        return applicationId;
    }

    @Override
    public Optional<Long> getCommandId() {
        return commandId != applicationId ? Optional.of(commandId) : Optional.empty();
    }

    @Override
    public Set<ApplicationCommandPermissions> getUpdatedPermissions() {
        return Collections.unmodifiableSet(newPermissions);
    }
}
