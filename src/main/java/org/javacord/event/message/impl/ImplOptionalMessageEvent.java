package org.javacord.event.message.impl;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.message.Message;
import org.javacord.event.message.OptionalMessageEvent;

import java.util.Optional;

/**
 * The implementation of {@link OptionalMessageEvent}.
 */
public abstract class ImplOptionalMessageEvent extends ImplMessageEvent implements OptionalMessageEvent {

    /**
     * The message of the event. Might be <code>null</code>.
     */
    private final Message message;

    /**
     * Creates a new optional message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public ImplOptionalMessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
        message = api.getCachedMessageById(messageId).orElse(null);
    }

    @Override
    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
    }

}
