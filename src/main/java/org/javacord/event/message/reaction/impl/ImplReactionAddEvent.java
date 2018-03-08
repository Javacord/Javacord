package org.javacord.event.message.reaction.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.message.Reaction;
import org.javacord.entity.user.User;
import org.javacord.event.message.reaction.ReactionAddEvent;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ReactionAddEvent}.
 */
public class ImplReactionAddEvent extends ImplSingleReactionEvent implements ReactionAddEvent {

    /**
     * Creates a new reaction add event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     * @param user The user who added the reaction.
     */
    public ImplReactionAddEvent(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user) {
        super(api, messageId, channel, emoji, user);
    }

    @Override
    public CompletableFuture<Void> removeReaction() {
        return Reaction.removeUser(getApi(), getChannel().getId(), getMessageId(), getEmoji(), getUser());
    }

}
