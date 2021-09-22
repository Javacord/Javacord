package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandBuilder<T extends ApplicationCommand> {

    /**
     * Gets the delegate used by the application command builder internally.
     *
     * @return The delegate used by this application command builder internally.
     */
    ApplicationCommandBuilderDelegate<T> getDelegate();

    /**
     * Creates a global application command.
     * When used to update multiple global slash commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built application command.
     */
    CompletableFuture<T> createGlobal(DiscordApi api);

    /**
     * Creates an application command for a specific server.
     * When used to create multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param server The server.
     * @return The built application command.
     */
    CompletableFuture<T> createForServer(Server server);
}
