package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SlashCommandUpdaterDelegate {

    /**
     * Sets the new name of the slash command.
     *
     * @param name The name to set.
     */
    void setName(String name);

    /**
     * Sets the new description of the slash command.
     *
     * @param description The description to set.
     */
    void setDescription(String description);

    /**
     * Sets the new slash command options.
     *
     * @param slashCommandOptions The slash command options to set.
     */
    void setOptions(List<SlashCommandOption> slashCommandOptions);

    /**
     * Sets the new slash command default permission.
     *
     * @param defaultPermission The default permission to set.
     */
    void setDefaultPermission(boolean defaultPermission);

    /**
     * Performs the queued update.
     *
     * @param api The DiscordApi.
     * @return A future with the updated slash command to check if the update was successful.
     */
    CompletableFuture<SlashCommand> updateGlobal(DiscordApi api);

    /**
     * Performs the queued update.
     *
     * @param server The server where the command should be updated.
     * @return A future with the updated slash command to check if the update was successful.
     */
    CompletableFuture<SlashCommand> updateForServer(Server server);
}
