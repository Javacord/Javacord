package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;

import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandUpdaterDelegate<T extends ApplicationCommand> {
    /**
     * Sets the new name of the application command.
     *
     * @param name The name to set.
     */
    void setName(String name);

    /**
     * Sets the new application command default permission.
     *
     * @param defaultPermission The default permission to set.
     */
    void setDefaultPermission(boolean defaultPermission);

    /**
     * Performs the queued update.
     *
     * @param api The DiscordApi.
     * @return A future with the updated application command to check if the update was successful.
     */
    CompletableFuture<T> updateGlobal(DiscordApi api);

    /**
     * Performs the queued update.
     *
     * @param server The server where the command should be updated.
     * @return A future with the updated application command to check if the update was successful.
     */
    CompletableFuture<T> updateForServer(Server server);
}
