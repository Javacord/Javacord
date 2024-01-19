package org.javacord.core.event.message.reaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.core.entity.user.Member;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ReactionAddEvent}.
 */
public class ReactionAddEventImpl extends SingleReactionEventImpl implements ReactionAddEvent {

    /**
     * The id of the user that sent the message.
     */
    private final Long messageAuthorId;

    /**
     * Creates a new reaction add event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     * @param userId The id of the user who added the reaction.
     * @param member The member if it happened in a server.
     * @param messageAuthorId The id of the user that sent the message
     */
    public ReactionAddEventImpl(DiscordApi api, long messageId, TextChannel channel, Emoji emoji,
                                long userId, Member member, Long messageAuthorId) {
        super(api, messageId, channel, emoji, userId);
        this.messageAuthorId = messageAuthorId;
    }

    @Override
    public CompletableFuture<Void> removeReaction() {
        return Reaction.removeUser(getApi(), getChannel().getId(), getMessageId(), getEmoji(), getUserId());
    }

    @Override
    public Optional<Long> getMessageAuthorId() {
        return Optional.ofNullable(messageAuthorId);
    }
}
