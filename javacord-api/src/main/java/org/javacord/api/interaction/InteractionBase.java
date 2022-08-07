package org.javacord.api.interaction;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
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
     *         that should be used to update the message later on.
     */
    CompletableFuture<InteractionOriginalResponseUpdater> respondLater();

    /**
     * Sends an acknowledgement of the interaction to Discord and displays an (ephemeral) loading state to the user,
     * indicating that you'll respond with a delay.
     * Please note that you can only actually update this loading state message within 15 minutes after receiving the
     * interaction.
     *
     * @param ephemeral Whether the response should be ephemeral
     * @return A CompletableFuture that completes as soon as the acknowledgement has been sent; it yields an updater
     *         that should be used to update the message later on.
     */
    CompletableFuture<InteractionOriginalResponseUpdater> respondLater(boolean ephemeral);

    /**
     * Respond with a popup modal the user can interact with.
     * You can respond to every interaction with a modal except to a {@link ModalInteraction}.
     *
     * @param customId   The custom ID of the modal.
     * @param title      The title of the modal.
     * @param components The components that should be displayed in the modal.
     * @return A CompletableFuture that completes as soon as the modal has been sent.
     */
    default CompletableFuture<Void> respondWithModal(String customId, String title, HighLevelComponent... components) {
        return respondWithModal(customId, title, Arrays.asList(components));
    }

    /**
     * Respond with a popup modal the user can interact with.
     * You can respond to every interaction with a modal except to a {@link ModalInteraction}.
     *
     * @param customId   The custom ID of the modal.
     * @param title      The title of the modal.
     * @param components The components that should be displayed in the modal.
     * @return A CompletableFuture that completes as soon as the modal has been sent.
     */
    CompletableFuture<Void> respondWithModal(String customId, String title, List<HighLevelComponent> components);

    /**
     * Create a message builder to send follow-up messages for this interaction.
     * You can send, edit and delete follow-up messages for up to 15 minutes after you received the interaction.
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

    /**
     * Gets the selected language of the invoking user.
     *
     * @return The user selected language.
     */
    DiscordLocale getLocale();

    /**
     * Gets the server's preferred locale, if invoked in a server.
     *
     * @return The server's preferred locale.
     */
    Optional<DiscordLocale> getServerLocale();

    /**
     * Gets the permissions the bot has within the channel the interaction was sent from.
     * Not present if the interaction has been invoked in a direct message.
     *
     * @return The bots permissions within the channel the interaction was sent from.
     */
    Optional<EnumSet<PermissionType>> getBotPermissions();
}
