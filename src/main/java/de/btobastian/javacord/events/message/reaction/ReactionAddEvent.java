package de.btobastian.javacord.events.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.emoji.Emoji;

import java.util.concurrent.CompletableFuture;

/**
 * A reaction add event.
 */
public class ReactionAddEvent extends SingleReactionEvent {

    /**
     * Creates a new reaction add event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     * @param user The user who added the reaction.
     */
    public ReactionAddEvent(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user) {
        super(api, messageId, channel, emoji, user);
    }

    /**
     * Removes the added reaction.
     *
     * @return A future to tell us if the action was successful.
     */
    public CompletableFuture<Void> removeReaction() {
        return Reaction.removeUser(getApi(), getChannel().getId(), getMessageId(), getEmoji(), getUser());
    }

}
