package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.UserContextMenuUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserContextMenuUpdater implements ApplicationCommandUpdater<UserContextMenu> {

    /**
     * The account delegate used by this instance.
     */
    private final UserContextMenuUpdaterDelegate delegate;

    /**
     * Creates a new user context menu updater.
     *
     * @param commandId The user context menu id which should be updated.
     */
    public UserContextMenuUpdater(long commandId) {
        delegate = DelegateFactory.createUserContextMenuUpdaterDelegate(commandId);
    }

    /**
     * Sets the new name of the user context menu.
     *
     * @param name The name to set.
     * @return The current instance in order to chain call methods.
     */
    public UserContextMenuUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the new application command default permission. When set to `false` no one will be able to use this command
     * until you overwrite it.
     * Disallowing the usage of this command includes absolutely every user even Administrators, Server owners
     * and in direct messages.
     *
     * @param defaultPermission The default permission to set.
     * @return The current instance in order to chain call methods.
     */
    public UserContextMenuUpdater setDefaultPermission(boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Updates a global slash command.
     * When used to update multiple global user context menus at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @return The updated user context menu.
     */
    public CompletableFuture<UserContextMenu> updateGlobal(DiscordApi api) {
        return delegate.updateGlobal(api);
    }

    /**
     * Updates a user context menu on the given server.
     * When used to update multiple server user context menus at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param server The server where the context menu should be updated.
     * @return The updated user context menu.
     */
    public CompletableFuture<UserContextMenu> updateForServer(Server server) {
        return delegate.updateForServer(server);
    }
}
