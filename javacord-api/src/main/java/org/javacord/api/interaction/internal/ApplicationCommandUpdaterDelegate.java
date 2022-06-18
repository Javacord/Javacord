package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;

import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandUpdaterDelegate<T extends ApplicationCommand> {
    /**
     * Sets the new name of the application command.
     *
     * @param name The name to set.
     */
    void setName(String name);

    /**
     * Adds a name localization for the given locale.
     *
     * @param locale The locale to add a localization for.
     * @param localization The command name localization.
     */
    void addNameLocalization(DiscordLocale locale, String localization);

    /**
     * Sets the new description of the slash command.
     *
     * @param description The description to set.
     */
    void setDescription(String description);

    /**
     * Adds a description localization for the given locale.
     *
     * @param locale The locale to add this localization for.
     * @param localization The command description localization.
     */
    void addDescriptionLocalization(DiscordLocale locale, String localization);

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
    default CompletableFuture<T> updateForServer(Server server) {
        return updateForServer(server.getApi(), server.getId());
    }

    /**
     * Performs the queued update.
     *
     * @param api The DiscordApi.
     * @param server The server where the command should be updated.
     * @return A future with the updated application command to check if the update was successful.
     */
    CompletableFuture<T> updateForServer(DiscordApi api, long server);
}
