package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.SlashCommandBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new slash commands.
 */
public class SlashCommandBuilder {

    private final SlashCommandBuilderDelegate delegate =
            DelegateFactory.createSlashCommandBuilderDelegate();

    /**
     * Creates a new slash command builder.
     */
    public SlashCommandBuilder() { }

    /**
     * Sets the name of the slash command.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the slash command.
     *
     * @param description The name.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Adds an slash command option to the slash command.
     *
     * @param option The option.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder addOption(SlashCommandOption option) {
        delegate.addOption(option);
        return this;
    }

    /**
     * Sets the slash commands for the slash command.
     *
     * @param options The options.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder setOptions(List<SlashCommandOption> options) {
        delegate.setOptions(options);
        return this;
    }

    /**
     * Sets the default permission for the slash command
     * whether the command is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandBuilder setDefaultPermission(Boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Creates a global slash command.
     * When used to update multiple global slash commands at once
     * {@link DiscordApi#bulkOverwriteGlobalSlashCommands(List)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built slash command.
     */
    public CompletableFuture<SlashCommand> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates an slash command for a specific server.
     * When used to create multiple server slash commands at once
     * {@link DiscordApi#bulkOverwriteServerSlashCommands(Server, List)} (List)} should be used instead.
     *
     * @param server The server.
     * @return The built slash command.
     */
    public CompletableFuture<SlashCommand> createForServer(Server server) {
        return delegate.createForServer(server);
    }

    /**
     * Gets the delegate used by the slash command builder internally.
     *
     * @return The delegate used by this slash command builder internally.
     */
    public SlashCommandBuilderDelegate getDelegate() {
        return delegate;
    }
}
