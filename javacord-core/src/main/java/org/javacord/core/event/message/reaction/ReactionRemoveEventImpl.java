package org.javacord.core.event.message.reaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ReactionRemoveEvent}.
 */
public class ReactionRemoveEventImpl extends SingleReactionEventImpl implements ReactionRemoveEvent {

    /**
     * Creates a new reaction remove event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     * @param user The user whose reaction got removed.
     * @param userId The id of the user whose reaction got removed.
     */
    public ReactionRemoveEventImpl(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user,
                                   long userId) {
        super(api, messageId, channel, emoji, user, userId);
    }

    @Override
    public Optional<User> getUser() {
        return Optional.ofNullable(this.user);
    }

    @Override
    public CompletableFuture<User> requestUser() {
        return getUser().map(CompletableFuture::completedFuture)
                .orElseGet(() -> api.getUserById(this.userId));
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
