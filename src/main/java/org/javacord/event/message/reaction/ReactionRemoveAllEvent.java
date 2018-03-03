package org.javacord.event.message.reaction;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;

/**
 * A reaction remove all event.
 */
public class ReactionRemoveAllEvent extends ReactionEvent {

    /**
     * Creates a new reaction remove all event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ReactionRemoveAllEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
