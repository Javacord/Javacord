package de.btobastian.javacord.event.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.channel.TextChannel;
import de.btobastian.javacord.entity.emoji.Emoji;
import de.btobastian.javacord.entity.user.User;

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
