package org.javacord.event.message;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;

/**
 * A message delete event.
 */
public class MessageDeleteEvent extends OptionalMessageEvent {

    /**
     * Creates a new message delete event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageDeleteEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
