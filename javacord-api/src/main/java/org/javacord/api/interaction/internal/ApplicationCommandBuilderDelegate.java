package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.ApplicationCommandOption;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link org.javacord.api.interaction.ApplicationCommandBuilder}.
 * You usually don't want to interact with this object.
 */
public interface ApplicationCommandBuilderDelegate {

    /**
     * Sets the name of the application command.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the description of the application command.
     *
     * @param description The name.
     */
    void setDescription(String description);

    /**
     * Adds an application command option to the application command.
     *
     * @param option The option.
     */
    void addOption(ApplicationCommandOption option);

    /**
     * Sets the application commands for the application command.
     *
     * @param options The options.
     */
    void setOptions(List<ApplicationCommandOption> options);

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
    CompletableFuture<ApplicationCommand> createGlobal(DiscordApi api);

    /**
     * Creates an application command for a specific server.
     *
     * @param server The server.
     * @return The built application command.
     */
    CompletableFuture<ApplicationCommand> createForServer(Server server);
}
