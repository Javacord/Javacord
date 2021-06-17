package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new application commands.
 */
public class ApplicationCommandBuilder {

    private final ApplicationCommandBuilderDelegate delegate =
            DelegateFactory.createApplicationCommandBuilderDelegate();

    /**
     * Creates a new application command builder.
     */
    public ApplicationCommandBuilder() { }

    /**
     * Sets the name of the application command.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the application command.
     *
     * @param description The name.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Adds an application command option to the application command.
     *
     * @param option The option.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandBuilder addOption(ApplicationCommandOption option) {
        delegate.addOption(option);
        return this;
    }

    /**
     * Sets the application commands for the application command.
     *
     * @param options The options.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandBuilder setOptions(List<ApplicationCommandOption> options) {
        delegate.setOptions(options);
        return this;
    }

    /**
     * Sets the default permission for the application command
     * whether the command is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandBuilder setDefaultPermission(Boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Creates a global application command.
     * When used to update multiple global application commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built application command.
     */
    public CompletableFuture<ApplicationCommand> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates an application command for a specific server.
     * When used to create multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} (List)} should be used instead.
     *
     * @param server The server.
     * @return The built application command.
     */
    public CompletableFuture<ApplicationCommand> createForServer(Server server) {
        return delegate.createForServer(server);
    }

    /**
     * Gets the delegate used by the application command builder internally.
     *
     * @return The delegate used by this application command builder internally.
     */
    public ApplicationCommandBuilderDelegate getDelegate() {
        return delegate;
    }
}
