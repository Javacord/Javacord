package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.MessageContextMenuBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new message context menus.
 */
public class MessageContextMenuBuilder implements ApplicationCommandBuilder<MessageContextMenu> {

    private final MessageContextMenuBuilderDelegate delegate =
            DelegateFactory.createMessageContextMenuBuilderDelegate();

    /**
     * Creates a new message context menu builder.
     */
    public MessageContextMenuBuilder() { }

    /**
     * Sets the name of the message context menu.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public MessageContextMenuBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the default permission for the message context menu
     * whether the context menu is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     * @return The current instance in order to chain call methods.
     */
    public MessageContextMenuBuilder setDefaultPermission(Boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Creates a global message context menu.
     * When used to update multiple global message context menus at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built message context menu.
     */
    public CompletableFuture<MessageContextMenu> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates a message context menu for a specific server.
     * When used to create multiple server message context menus at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param server The server.
     * @return The built message context menu.
     */
    public CompletableFuture<MessageContextMenu> createForServer(Server server) {
        return delegate.createForServer(server);
    }

    /**
     * Gets the delegate used by the message context menu builder internally.
     *
     * @return The delegate used by this message context menu builder internally.
     */
    public MessageContextMenuBuilderDelegate getDelegate() {
        return delegate;
    }
}
