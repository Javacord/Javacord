package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.entities.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents either a user or a webhook.
 */
public interface MessageAuthor extends DiscordEntity, AvatarHolder {

    /**
     * Gets the message.
     *
     * @return The message.
     */
    Message getMessage();

    /**
     * Gets the name of the author.
     *
     * @return The name of the author.
     */
    String getName();

    /**
     * Gets the display name of the author.
     *
     * @return The display name of the author.
     */
    default String getDisplayName() {
        Optional<Server> server = getMessage().getServer();
        Optional<User> user = asUser();
        if (user.isPresent()) {
            return server.map(s -> user.get().getDisplayName(s)).orElseGet(() -> user.get().getName());
        }
        return getName();
    }

    /**
     * Gets the discriminator of the author.
     *
     * @return The discriminator of the author.
     */
    String getDiscriminator();

    /**
     * Checks if the author of the message is a user.
     *
     * @return Whether the author is a user or not.
     */
    boolean isUser();

    /**
     * Gets the author as user.
     *
     * @return The author as user.
     */
    default Optional<User> asUser() {
        if (isUser()) {
            return getApi().getUserById(getId());
        }
        return Optional.empty();
    }

    /**
     * Checks if the author is a webhook.
     *
     * @return Whether the author is a webhook or not.
     */
    boolean isWebhook();

    /**
     * Gets the author as a webhook.
     *
     * @return The author as a webhook.
     */
    default Optional<CompletableFuture<Webhook>> asWebhook() {
        if (isWebhook()) {
            return Optional.of(getApi().getWebhookById(getId()));
        }
        return Optional.empty();
    }

}
