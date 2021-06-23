package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link SlashCommandBuilder}.
 * You usually don't want to interact with this object.
 */
public interface SlashCommandBuilderDelegate {

    /**
     * Sets the name of the slash command.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the description of the slash command.
     *
     * @param description The name.
     */
    void setDescription(String description);

    /**
     * Adds an slash command option to the slash command.
     *
     * @param option The option.
     */
    void addOption(SlashCommandOption option);

    /**
     * Sets the slash commands for the slash command.
     *
     * @param options The options.
     */
    void setOptions(List<SlashCommandOption> options);

    /**
     * Sets the default permission for the slash command
     * whether the command is enabled by default when the app is added to a server.
     *
     * @param defaultPermission The default permission.
     */
    void setDefaultPermission(Boolean defaultPermission);

    /**
     * Creates a global slash command.
     *
     * @param api The discord api instance.
     * @return The built slash command.
     */
    CompletableFuture<SlashCommand> createGlobal(DiscordApi api);

    /**
     * Creates an slash command for a specific server.
     *
     * @param server The server.
     * @return The built slash command.
     */
    CompletableFuture<SlashCommand> createForServer(Server server);
}
