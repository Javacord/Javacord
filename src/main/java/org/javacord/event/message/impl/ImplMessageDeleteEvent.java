package org.javacord.event.message.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.event.message.MessageDeleteEvent;

/**
 * The implementation of {@link MessageDeleteEvent}.
 */
public class ImplMessageDeleteEvent extends ImplOptionalMessageEvent implements MessageDeleteEvent {

    /**
     * Creates a new message delete event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ImplMessageDeleteEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
    }

}
