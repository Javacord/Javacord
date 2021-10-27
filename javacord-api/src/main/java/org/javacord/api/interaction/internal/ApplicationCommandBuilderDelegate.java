package org.javacord.api.interaction.internal;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ApplicationCommand;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandBuilderDelegate<T extends ApplicationCommand> {

    /**
     * Sets the name of the application command.
     *
     * @param name The name.
     */
    void setName(String name);

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
     * @param server The server.
     * @return The built application command.
     */
    CompletableFuture<T> createForServer(Server server);

}
