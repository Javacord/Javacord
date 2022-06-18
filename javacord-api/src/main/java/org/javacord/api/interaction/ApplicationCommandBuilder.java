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
     * Adds a name localization for the given locale.
     *
     * @param locale The locale to add this localization for.
     * @param localization The command name localization.
     * @return The current instance in order to chain call methods.
     */
    public T addNameLocalization(DiscordLocale locale, String localization) {
        delegate.addNameLocalization(locale, localization);
        return (T) this;
    }

    /**
     * Sets the description of the slash command.
     *
     * @param description The description.
     * @return The current instance in order to chain call methods.
     */
    public T setDescription(String description) {
        delegate.setDescription(description);
        return (T) this;
    }

    /**
     * Adds a description localization for the given locale.
     *
     * @param locale The locale to add a localization for.
     * @param localization The command description localization.
     * @return The current instance in order to chain call methods.
     */
    public T addDescriptionLocalization(DiscordLocale locale, String localization) {
        delegate.addDescriptionLocalization(locale, localization);
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
        return delegate.createForServer(server.getApi(), server.getId());
    }


    /**
     * Creates an application command for a specific server.
     * When used to create multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param api The discord api instance.
     * @param server The server.
     * @return The built application command.
     */
    public CompletableFuture<R> createForServer(DiscordApi api, long server) {
        return delegate.createForServer(api, server);
    }

}
