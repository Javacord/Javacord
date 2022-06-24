package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import java.util.EnumSet;

public interface UserContextMenu extends ContextMenu {

    /**
     * Create a new user context menu builder with the given name and description.
     * Call {@link UserContextMenuBuilder#createForServer(Server)} or
     * {@link UserContextMenuBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name        The name of the new user context menu command.
     * @param description The description of the new user context menu command.
     * @return The new user context menu builder
     */
    static UserContextMenuBuilder with(String name, String description) {
        return new UserContextMenuBuilder()
                .setName(name)
                .setDescription(description);
    }

    /**
     * Create a new user context menu command builder with the given name and description.
     * Call {@link UserContextMenuBuilder#createForServer(Server)} or
     * {@link UserContextMenuBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new user context menu command.
     * @param description         The description of the new user context menu command.
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new user context menu builder
     */
    static UserContextMenuBuilder withRequiredPermissions(String name, String description,
                                                          PermissionType... requiredPermissions) {
        return new UserContextMenuBuilder()
                .setName(name)
                .setDefaultEnabledForPermissions(requiredPermissions)
                .setDescription(description);
    }

    /**
     * Create a new user context menu command builder with the given name and description.
     * Call {@link UserContextMenuBuilder#createForServer(Server)} or
     * {@link UserContextMenuBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new user context menu command.
     * @param description         The description of the new user context menu command.
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new user context menu builder
     */
    static UserContextMenuBuilder withRequiredPermissions(String name, String description,
                                                          EnumSet<PermissionType> requiredPermissions) {
        return new UserContextMenuBuilder()
                .setName(name)
                .setDefaultEnabledForPermissions(requiredPermissions)
                .setDescription(description);
    }

    /**
     * Creates a user context menu updater from this UserContextMenu instance.
     *
     * @return The user context menu updater for this UserContextMenu instance.
     */
    default UserContextMenuUpdater createUserContextMenuUpdater() {
        return new UserContextMenuUpdater(this.getId());
    }
}
