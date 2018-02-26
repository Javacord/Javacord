package de.btobastian.javacord.events.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.events.message.RequestableMessageEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A single reaction event.
 */
public class SingleReactionEvent extends ReactionEvent {

    /**
     * The emoji of the event.
     */
    private final Emoji emoji;

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new single reaction event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     * @param user The "owner" of the reaction.
     */
    public SingleReactionEvent(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user) {
        super(api, messageId, channel);
        this.emoji = emoji;
        this.user = user;
    }

    /**
     * Gets the emoji of the event.
     *
     * @return The emoji.
     */
    public Emoji getEmoji() {
        return emoji;
    }

    /**
     * Gets the "owner" of the reaction.
     *
     * @return The "owner" of the reaction..
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the reaction if the message is cached and the reaction exists.
     *
     * @return The reaction.
     */
    public Optional<Reaction> getReaction() {
        return getMessage().flatMap(msg -> msg.getReactionByEmoji(getEmoji()));
    }

    /**
     * Gets the reaction if it exists.
     * If the message is not cached, it will be requested from Discord first.
     *
     * @return The reaction.
     * @see RequestableMessageEvent#requestMessage()
     */
    public CompletableFuture<Optional<Reaction>> requestReaction() {
        return requestMessage().thenApply(msg -> msg.getReactionByEmoji(getEmoji()));
    }

    /**
     * Gets the amount of users who used the reaction if the message is cached.
     *
     * @return The amount of users who used the reaction.
     */
    public Optional<Integer> getCount() {
        return getMessage().map(msg -> msg.getReactionByEmoji(getEmoji()).map(Reaction::getCount).orElse(0));
    }

    /**
     * Gets the amount of users who used the reaction.
     * If the message is not cached, it will be requested from Discord first.
     *
     * @return The amount of users who used the reaction.
     * @see RequestableMessageEvent#requestMessage()
     */
    public CompletableFuture<Integer> requestCount() {
        return requestMessage().thenApply(msg -> msg.getReactionByEmoji(getEmoji()).map(Reaction::getCount).orElse(0));
    }

    /**
     * Gets a list with all users who used the reaction.
     *
     * @return A list with all users who used the reaction.
     */
    public CompletableFuture<List<User>> getUsers() {
        return Reaction.getUsers(getApi(), getChannel().getId(), getMessageId(), getEmoji());
    }

}
