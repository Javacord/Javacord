package org.javacord.core.event.message.reaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.SingleReactionEvent;
import org.javacord.core.event.message.RequestableMessageEventImpl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link SingleReactionEvent}.
 */
public abstract class SingleReactionEventImpl extends RequestableMessageEventImpl implements SingleReactionEvent {

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
    public SingleReactionEventImpl(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user) {
        super(api, messageId, channel);
        this.emoji = emoji;
        this.user = user;
    }

    @Override
    public Emoji getEmoji() {
        return emoji;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Optional<Reaction> getReaction() {
        return getMessage().flatMap(msg -> msg.getReactionByEmoji(getEmoji()));
    }

    @Override
    public CompletableFuture<Optional<Reaction>> requestReaction() {
        return requestMessage().thenApply(msg -> msg.getReactionByEmoji(getEmoji()));
    }

    @Override
    public Optional<Integer> getCount() {
        return getMessage().map(msg -> msg.getReactionByEmoji(getEmoji()).map(Reaction::getCount).orElse(0));
    }

    @Override
    public CompletableFuture<Integer> requestCount() {
        return requestMessage().thenApply(msg -> msg.getReactionByEmoji(getEmoji()).map(Reaction::getCount).orElse(0));
    }

    @Override
    public CompletableFuture<List<User>> getUsers() {
        return Reaction.getUsers(getApi(), getChannel().getId(), getMessageId(), getEmoji());
    }

}
