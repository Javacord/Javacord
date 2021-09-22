package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandUpdater<T extends ApplicationCommand> {

    /**
     * Updates a global application command.
     * When used to update multiple global application commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(List)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @return The updated application command.
     */
    CompletableFuture<T> updateGlobal(DiscordApi api);

    /**
     * Updates an application command on the given server.
     * When used to update multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, List)} should be used instead.
     *
     * @param server The server where the command should be updated.
     * @return The updated application command.
     */
    CompletableFuture<T> updateForServer(Server server);
}
