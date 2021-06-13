package org.javacord.api.interaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerApplicationCommandPermissionsBuilder {

    private final long commandId;
    private final List<ApplicationCommandPermissions> permissions;

    /**
     * Creates an instance of this server application command permissions builder.
     *
     * @param commandId The command ID which should be updated.
     * @param permissions The permissions for the command which should be updated.
     */
    public ServerApplicationCommandPermissionsBuilder(long commandId, List<ApplicationCommandPermissions> permissions) {
        this.commandId = commandId;
        this.permissions = new ArrayList<>(permissions);
    }

    /**
     * Gets the command ID.
     *
     * @return The command ID.
     */
    public long getCommandId() {
        return commandId;
    }

    /**
     * Gets a list of the application command permissions.
     *
     * @return A list containing the application command permissions.
     */
    public List<ApplicationCommandPermissions> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }
}
