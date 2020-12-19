package org.javacord.api.command;

import org.javacord.api.DiscordApi;
import org.javacord.api.command.internal.ApplicationCommandBuilderDelegate;
import org.javacord.api.entity.server.Server;
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
     * Creates a global application command.
     *
     * @param api The discord api instance.
     * @return The built application command.
     */
    public CompletableFuture<ApplicationCommand> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates an application command for a specific server.
     *
     * @param server The server.
     * @return The built application command.
     */
    public CompletableFuture<ApplicationCommand> createForServer(Server server) {
        return delegate.createForServer(server);
    }
}
