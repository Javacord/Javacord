package org.javacord.event.message.reaction;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.user.User;

/**
 * A reaction remove event.
 */
public class ReactionRemoveEvent extends SingleReactionEvent {

    /**
     * Creates a new reaction remove event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     * @param emoji The emoji.
     * @param user The user whose reaction got removed.
     */
    public ReactionRemoveEvent(DiscordApi api, long messageId, TextChannel channel, Emoji emoji, User user) {
        super(api, messageId, channel, emoji, user);
    }

}
