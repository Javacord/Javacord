package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import java.util.EnumSet;

public interface MessageContextMenu extends ContextMenu {

    /**
     * Create a new message context menu command builder with the given name and description.
     * Call {@link MessageContextMenuBuilder#createForServer(Server)} or
     * {@link MessageContextMenuBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name        The name of the new message context menu command.
     * @param description The description of the new message context menu command.
     * @return The new message context menu builder
     */
    static MessageContextMenuBuilder with(String name, String description) {
        return new MessageContextMenuBuilder()
                .setName(name)
                .setDescription(description);
    }


    /**
     * Create a new message context menu command builder with the given name and description.
     * Call {@link MessageContextMenuBuilder#createForServer(Server)} or
     * {@link MessageContextMenuBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new message context menu command.
     * @param description         The description of the new message context menu command.
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new message context menu builder
     */
    static MessageContextMenuBuilder withRequiredPermissions(String name, String description,
                                                             PermissionType... requiredPermissions) {
        return new MessageContextMenuBuilder()
                .setName(name)
                .setDefaultEnabledForPermissions(requiredPermissions)
                .setDescription(description);
    }

    /**
     * Create a new message context menu command builder with the given name and description.
     * Call {@link MessageContextMenuBuilder#createForServer(Server)} or
     * {@link MessageContextMenuBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new message context menu command.
     * @param description         The description of the new message context menu command.
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new message context menu builder
     */
    static MessageContextMenuBuilder withRequiredPermissions(String name, String description,
                                                             EnumSet<PermissionType> requiredPermissions) {
        return new MessageContextMenuBuilder()
                .setName(name)
                .setDefaultEnabledForPermissions(requiredPermissions)
                .setDescription(description);
    }

    /**
     * Creates a message context menu updater from this MessageContextMenu instance.
     *
     * @return The message context menu updater for this MessageContextMenu instance.
     */
    default MessageContextMenuUpdater createMessageContextMenuUpdater() {
        return new MessageContextMenuUpdater(this.getId());
    }

}
