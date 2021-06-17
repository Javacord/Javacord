package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationCommandUpdater {

    /**
     * The account delegate used by this instance.
     */
    private final ApplicationCommandUpdaterDelegate delegate;

    /**
     * Creates a new application command updater.
     *
     * @param commandId The application command id which should be updated.
     */
    public ApplicationCommandUpdater(long commandId) {
        delegate = DelegateFactory.createApplicationCommandUpdaterDelegate(commandId);
    }

    /**
     * Sets the new name of the application command.
     *
     * @param name The name to set.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the new description of the application command.
     *
     * @param description The description to set.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandUpdater setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the new application command options.
     *
     * @param applicationCommandOptions The application command options to set.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandUpdater setApplicationCommandOptions(
            List<ApplicationCommandOption> applicationCommandOptions) {
        delegate.setOptions(applicationCommandOptions);
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
    public ApplicationCommandUpdater setDefaultPermission(boolean defaultPermission) {
        delegate.setDefaultPermission(defaultPermission);
        return this;
    }

    /**
     * Updates a global application command.
     * When used to update multiple global application commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @return The updated Application command.
     */
    public CompletableFuture<ApplicationCommand> updateGlobal(DiscordApi api) {
        return delegate.updateGlobal(api);
    }

    /**
     * Updates an application command on the given server.
     * When used to update multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} (List)} should be used instead.
     *
     * @param server The server where the command should be updated.
     * @return The updated Application command.
     */
    public CompletableFuture<ApplicationCommand> updateForServer(Server server) {
        return delegate.updateForServer(server);
    }
}
