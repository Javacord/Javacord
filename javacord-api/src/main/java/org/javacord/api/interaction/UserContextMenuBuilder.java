package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.UserContextMenuBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new user context menus.
 */
public class UserContextMenuBuilder implements ApplicationCommandBuilder<UserContextMenu> {

    private final UserContextMenuBuilderDelegate delegate =
            DelegateFactory.createUserContextMenuBuilderDelegate();

    /**
     * Creates a new user context menu builder.
     */
    public UserContextMenuBuilder() { }

    /**
     * Sets the name of the user context menu.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public UserContextMenuBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the default permission for the user context menu
     * whether the context menu is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     * @return The current instance in order to chain call methods.
     */
    public UserContextMenuBuilder setDefaultPermission(Boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Creates a global user context menu.
     * When used to update multiple global user context menus at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built user context menu.
     */
    public CompletableFuture<UserContextMenu> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates a user context menu for a specific server.
     * When used to create multiple server user context menu at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param server The server.
     * @return The built user context menu.
     */
    public CompletableFuture<UserContextMenu> createForServer(Server server) {
        return delegate.createForServer(server);
    }

    /**
     * Gets the delegate used by the user context menu builder internally.
     *
     * @return The delegate used by this user context menu builder internally.
     */
    public UserContextMenuBuilderDelegate getDelegate() {
        return delegate;
    }
}
