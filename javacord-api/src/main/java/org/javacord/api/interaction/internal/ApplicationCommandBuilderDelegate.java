package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.DiscordLocale;

import java.util.EnumSet;
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
     * Sets the default required permissions for this command.
     * This can later be overridden by server admins.
     *
     * @param requiredPermissions The required permissions to use this command.
     */
    void setDefaultEnabledForPermissions(EnumSet<PermissionType> requiredPermissions);

    /**
     * Enables this command for use by all users.
     * This can later be overridden by server admins.
     */
    void setDefaultEnabledForEveryone();

    /**
     * Sets whether this command should be disabled and only usable by server administrators by default.
     * This can later be overridden by server administrators.
     */
    void setDefaultDisabled();

    /**
     * Sets whether this command is able to be used in DMs. By default, this is {@code true}
     * This has no effect on server commands.
     *
     * @param enabledInDms Whether the command is enabled in DMs.
     */
    void setEnabledInDms(boolean enabledInDms);

    /**
     * Sets the slash command as nsfw.
     *
     * @param nsfw Whether the command is nsfw.
     */
    void setNsfw(boolean nsfw);

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
