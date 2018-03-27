package org.javacord.core.event.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageDeleteEvent;

/**
 * The implementation of {@link MessageDeleteEvent}.
 */
public class MessageDeleteEventImpl extends OptionalMessageEventImpl implements MessageDeleteEvent {

    /**
     * Creates a new message delete event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageDeleteEventImpl(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
