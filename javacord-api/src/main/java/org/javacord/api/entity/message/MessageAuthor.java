package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.Webhook;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents either a user or a webhook.
 *
 * <p>Do not confuse a webhook with a bot account which is also considered to be a user.
 */
public interface MessageAuthor extends DiscordEntity, Nameable {

    /**
     * Gets the webhook id of this author if the message was sent by a webhook.
     *
     * @return The webhook id of this author if the message was sent by a webhook.
     */
    Optional<Long> getWebhookId();

    /**
     * Gets the message.
     *
     * @return The message.
     */
    Message getMessage();

    /**
     * Gets the display name of the author.
     *
     * @return The display name of the author.
     */
    default String getDisplayName() {
        Optional<Server> serverOptional = getMessage().getServer();
        Optional<User> userOptional = asUser();

        if (userOptional.isPresent()) {
            return serverOptional.flatMap(s -> s.getMemberById(userOptional.get().getId())).map(Member::getDisplayName)
                    .orElseGet(() -> userOptional.get().getName());
        }
        return getName();
    }

    /**
     * If the author is a user, gets the discriminated name of the user, e.g. {@code Bastian#8222},
     * otherwise just gets the name of the author.
     *
     * @return The discriminated name of the user or the name of the author.
     */
    default String getDiscriminatedName() {
        return getDiscriminator().map(discriminator -> getName() + "#" + discriminator).orElseGet(this::getName);
    }

    /**
     * Gets the discriminator of the author if the author is a user.
     *
     * @return The discriminator of the author if the author is a user.
     */
    Optional<String> getDiscriminator();

    /**
     * Gets the avatar of the author.
     *
     * @return The avatar of the author.
     */
    Icon getAvatar();

    /**
     * Gets the avatar of the user.
     *
     * @param size The size of the image, must be any power of 2 between 16 and 4096.
     * @return The avatar of the user.
     */
    Icon getAvatar(int size);

    /**
     * Gets the voice channel this MessageAuthor (if it is a User)
     * is connected to on the server where the message has been sent.
     *
     * @return The server voice channel the MessageAuthor is connected to.
     */
    default Optional<ServerVoiceChannel> getConnectedVoiceChannel() {
        return getMessage().getServer().flatMap(server -> server.getConnectedVoiceChannel(getId()));
    }

    /**
     * Checks if the author of the message is a user.
     *
     * <p>A user might be a human user using any number of Discord client applications or a bot user connecting
     * via Discord's bot api. See {@link #isRegularUser} and {@link #isBotUser} for further detail.
     *
     * <p>The opposite of this method is {@link #isWebhook()}.
     *
     * @return Whether the author is a user or not.
     */
    boolean isUser();

    /**
     * Checks if the author is the owner of the current account.
     *
     * <p>Will return false if the account is owned by a team.</p>
     *
     * @return Whether the author is the owner of the current account.
     */
    default boolean isBotOwner() {
        return asUser().map(User::isBotOwner).orElse(false);
    }

    /**
     * Checks if the author is a member of the team owning the bot account.
     *
     * @return Whether the author is a member of the team owning the bot account.
     */
    default boolean isTeamMember() {
        return asUser().map(User::isTeamMember).orElse(false);
    }

    /**
     * Checks if the author is a bot user.
     *
     * <p>Keep in mind, that if this returns {@code false}, it does not mean that the message was sent by a regular
     * user. It's still possible that the message was sent via a webhook.
     *
     * @return Whether the author is a bot user.
     */
    default boolean isBotUser() {
        return asUser().map(User::isBot).orElse(false);
    }

    /**
     * Checks if the author is a regular user.
     *
     * <p>Keep in mind, that if this returns {@code false}, it does not mean that the message was sent by a bot
     * user. It's still possible that the message was sent via a webhook.
     *
     * @return Whether the author is a regular user.
     */
    default boolean isRegularUser() {
        return asUser().map(user -> !user.isBot()).orElse(false);
    }

    /**
     * Gets the author as user.
     *
     * @return The author as user.
     */
    Optional<User> asUser();

    /**
     * Gets the author as member. Only present if the member cache is enabled.
     *
     * @return The author as member.
     */
    Optional<Member> asMember();

    /**
     * Checks if the author is a webhook.
     *
     * <p>The opposite of this method is {@link #isUser()}.
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
        return asUser().map(User::isYourself).orElse(false);
    }

}
