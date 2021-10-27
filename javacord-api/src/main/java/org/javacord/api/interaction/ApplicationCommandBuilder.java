package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ApplicationCommandBuilder<R extends ApplicationCommand,
        D extends ApplicationCommandBuilderDelegate<R>, T extends ApplicationCommandBuilder<R, D, T>> {

    private final D delegate;

    protected ApplicationCommandBuilder(D delegate) {
        this.delegate = delegate;
    }

    /**
     * Sets the name of the message context menu.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public T setName(String name) {
        delegate.setName(name);
        return (T) this;
    }

    /**
     * Sets the default permission for the message context menu
     * whether the context menu is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     * @return The current instance in order to chain call methods.
     */
    public T setDefaultPermission(Boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return (T) this;
    }

    /**
     * Gets the delegate used by the application command builder internally.
     *
     * @return The delegate used by this application command builder internally.
     */
    public D getDelegate() {
        return delegate;
    }

    /**
     * Creates a global application command.
     * When used to update multiple global slash commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built application command.
     */
    public CompletableFuture<R> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates an application command for a specific server.
     * When used to create multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param server The server.
     * @return The built application command.
     */
    public CompletableFuture<R> createForServer(Server server) {
        return delegate.createForServer(server);
    }

}
