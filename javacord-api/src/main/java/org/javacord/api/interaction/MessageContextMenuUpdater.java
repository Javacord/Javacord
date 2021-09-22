package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.MessageContextMenuUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MessageContextMenuUpdater implements ApplicationCommandUpdater<MessageContextMenu> {
    /**
     * The account delegate used by this instance.
     */
    private final MessageContextMenuUpdaterDelegate delegate;

    /**
     * Creates a new message context menu updater.
     *
     * @param commandId The message context menu id which should be updated.
     */
    public MessageContextMenuUpdater(long commandId) {
        delegate = DelegateFactory.createMessageContextMenuUpdaterDelegate(commandId);
    }

    /**
     * Sets the new name of the message context menu.
     *
     * @param name The name to set.
     * @return The current instance in order to chain call methods.
     */
    public MessageContextMenuUpdater setName(String name) {
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
    public MessageContextMenuUpdater setDefaultPermission(boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Updates a global message context menu.
     * When used to update multiple global message context menus at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @return The updated message context menu.
     */
    public CompletableFuture<MessageContextMenu> updateGlobal(DiscordApi api) {
        return delegate.updateGlobal(api);
    }

    /**
     * Updates a message context menu on the given server.
     * When used to update multiple server message context menus at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} (List)} should be used instead.
     *
     * @param server The server where the command should be updated.
     * @return The updated message context menu.
     */
    public CompletableFuture<MessageContextMenu> updateForServer(Server server) {
        return delegate.updateForServer(server);
    }
}
