package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.SlashCommandUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SlashCommandUpdater {

    /**
     * The account delegate used by this instance.
     */
    private final SlashCommandUpdaterDelegate delegate;

    /**
     * Creates a new slash command updater.
     *
     * @param commandId The slash command id which should be updated.
     */
    public SlashCommandUpdater(long commandId) {
        delegate = DelegateFactory.createSlashCommandUpdaterDelegate(commandId);
    }

    /**
     * Sets the new name of the slash command.
     *
     * @param name The name to set.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the new description of the slash command.
     *
     * @param description The description to set.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandUpdater setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the new slash command options.
     *
     * @param slashCommandOptions The slash command options to set.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandUpdater setSlashCommandOptions(
            List<SlashCommandOption> slashCommandOptions) {
        delegate.setOptions(slashCommandOptions);
        return this;
    }

    /**
     * Sets the new slash command default permission. When set to `false` no one will be able to use this command
     * until you overwrite it.
     * Disallowing the usage of this command includes absolutely every user even Administrators, Server owners
     * and in direct messages.
     *
     * @param defaultPermission The default permission to set.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandUpdater setDefaultPermission(boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Updates a global slash command.
     * When used to update multiple global slash commands at once
     * {@link DiscordApi#bulkOverwriteGlobalSlashCommands(List)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @return The updated slash command.
     */
    public CompletableFuture<SlashCommand> updateGlobal(DiscordApi api) {
        return delegate.updateGlobal(api);
    }

    /**
     * Updates an slash command on the given server.
     * When used to update multiple server slash commands at once
     * {@link DiscordApi#bulkOverwriteServerSlashCommands(Server, List)} (List)} should be used instead.
     *
     * @param server The server where the command should be updated.
     * @return The updated slash command.
     */
    public CompletableFuture<SlashCommand> updateForServer(Server server) {
        return delegate.updateForServer(server);
    }
}
