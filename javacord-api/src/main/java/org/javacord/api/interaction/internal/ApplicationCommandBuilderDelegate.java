package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;

import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandBuilderDelegate<T extends ApplicationCommand> {

    /**
     * Sets the name of the application command.
     *
     * @param name The name.
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
     * Sets the description of the slash command.
     *
     * @param description The description.
     */
    void setDescription(String description);

    /**
     * Adds a description localization for the given locale.
     *
     * @param locale The locale to add a localization for.
     * @param localization The command description localization.
     */
    void addDescriptionLocalization(DiscordLocale locale, String localization);

    /**
     * Sets the default permission for the application command
     * whether the command is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     */
    void setDefaultPermission(Boolean defaultPermission);

    /**
     * Creates a global application command.
     *
     * @param api The discord api instance.
     * @return The built application command.
     */
    CompletableFuture<T> createGlobal(DiscordApi api);

    /**
     * Creates an application command for a specific server.
     *
     * @param api The discord api instance.
     * @param server The server.
     * @return The built application command.
     */
    CompletableFuture<T> createForServer(DiscordApi api, long server);

}
