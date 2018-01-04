package de.btobastian.javacord.entities.message;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents either a user or a webhook.
 */
public interface MessageAuthor extends DiscordEntity {

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
     * Gets the avatar of the author.
     *
     * @return The avatar of the author.
     */
    Icon getAvatar();

    /**
     * Checks if the author of the message is a user.
     *
     * @return Whether the author is a user or not.
     */
    boolean isUser();

    /**
     * Checks if the author is the owner of the current account.
     * Always returns <code>false</code> if logged in to a user account.
     *
     * @return Whether the author is the owner of the current account.
     */
    default boolean isBotOwner() {
        return getApi().getAccountType() == AccountType.BOT && isUser() && getApi().getOwnerId() == getId();
    }

    /**
     * Checks if the author is an administrator of the server where the message was sent.
     * Always returns {@code false} if the author is not a user or the message was not sent on a server.
     *
     * @return Whether the author is an administrator of the server or not.
     */
    default boolean isServerAdmin() {
        return getMessage()
                .getServer()
                .flatMap(server -> asUser().map(server::isAdmin))
                .orElse(false);
    }

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

    /**
     * Gets if this author is the user of the connected account.
     *
     * @return Whether this author is the user of the connected account or not.
     * @see DiscordApi#getYourself()
     */
    default boolean isYourself() {
        return getApi().getYourself().getId() == getId();
    }

}
