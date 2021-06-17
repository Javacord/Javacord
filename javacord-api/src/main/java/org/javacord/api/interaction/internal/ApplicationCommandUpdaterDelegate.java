package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.ApplicationCommandOption;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandUpdaterDelegate {

    /**
     * Sets the new name of the application command.
     *
     * @param name The name to set.
     */
    void setName(String name);

    /**
     * Sets the new description of the application command.
     *
     * @param description The description to set.
     */
    void setDescription(String description);

    /**
     * Sets the new application command options.
     *
     * @param applicationCommandOptions The application command options to set.
     */
    void setOptions(List<ApplicationCommandOption> applicationCommandOptions);

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
    CompletableFuture<ApplicationCommand> updateGlobal(DiscordApi api);

    /**
     * Performs the queued update.
     *
     * @param server The server where the command should be updated.
     * @return A future with the updated application command to check if the update was successful.
     */
    CompletableFuture<ApplicationCommand> updateForServer(Server server);
}
