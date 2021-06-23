package org.javacord.api.interaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerSlashCommandPermissionsBuilder {

    private final long commandId;
    private final List<SlashCommandPermissions> permissions;

    /**
     * Creates an instance of this server slash command permissions builder.
     *
     * @param commandId The command ID which should be updated.
     * @param permissions The permissions for the command which should be updated.
     */
    public ServerSlashCommandPermissionsBuilder(long commandId, List<SlashCommandPermissions> permissions) {
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
     * Gets a list of the slash command permissions.
     *
     * @return A list containing the slash command permissions.
     */
    public List<SlashCommandPermissions> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }
}
