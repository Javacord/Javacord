package org.javacord.event.message;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.message.Message;

import java.util.Optional;

/**
 * A message event where the message is NOT guaranteed to be in the cache.
 */
public abstract class OptionalMessageEvent extends MessageEvent {

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
    public OptionalMessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api, messageId, channel);
        message = api.getCachedMessageById(messageId).orElse(null);
    }

    /**
     * Gets the message from the cache.
     *
     * @return The message from the cache.
     */
    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
    }

}
