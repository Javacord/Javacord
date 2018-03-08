package org.javacord.event.message.reaction.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.message.Reaction;
import org.javacord.entity.user.User;
import org.javacord.event.message.impl.ImplRequestableMessageEvent;
import org.javacord.event.message.reaction.SingleReactionEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link SingleReactionEvent}.
 */
public abstract class ImplSingleReactionEvent extends ImplRequestableMessageEvent implements SingleReactionEvent {

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
    public ImplSingleReactionEvent(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user) {
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
