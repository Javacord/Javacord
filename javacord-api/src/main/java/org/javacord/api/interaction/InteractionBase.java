package org.javacord.api.interaction;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface InteractionBase extends DiscordEntity {

    /**
     * Gets the id of the application that this interaction is for.
     *
     * @return The id of the application.
     */
    long getApplicationId();

    /**
     * Gets the type of the interaction.
     *
     * @return The type of the interaction.
     */
    InteractionType getType();

    /**
     * Create a response builder that can be used to immediately reply to this interaction with a message.
     * Please note that you have to call <code>respond()</code> on the returned object within 3 seconds after receiving
     * the interaction.
     *
     * @return The new response builder.
     */
    InteractionImmediateResponseBuilder createImmediateResponder();

    /**
     * Sends an acknowledgement of the interaction to Discord and displays a loading state to the user, indicating that
     * you'll respond with a delay.
     * Please note that you can only actually update this loading state message within 15 minutes after receiving the
     * interaction.
     *
     * @return A CompletableFuture that completes as soon as the acknowledgement has been sent; it yields an updater
     *     that should be used to update the message later on.
     */
    CompletableFuture<InteractionOriginalResponseUpdater> respondLater();

    /**
     * Create a message builder to send follow up messages for this interaction.
     * You can send, edit and delete follow up messages up to 15 minutes after you received the interaction.
     *
     * @return The new message builder.
     */
    InteractionFollowupMessageBuilder createFollowupMessageBuilder();

    /**
     * Gets the server that this interaction was sent from.
     *
     * @return The server.
     */
    Optional<Server> getServer();

    /**
     * Gets the channel that this interaction was sent from.
     *
     * @return The channel.
     */
    Optional<TextChannel> getChannel();

    /**
     * Gets the invoking user.
     *
     * @return The invoking user.
     */
    User getUser();

    /**
     * Gets the continuation token for responding to the interaction.
     *
     * @return The token.
     */
    String getToken();

    /**
     * Gets the version.
     *
     * @return The version.
     */
    int getVersion();
}
