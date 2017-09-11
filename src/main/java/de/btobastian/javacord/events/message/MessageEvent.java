package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.events.Event;

/**
 * A message event.
 */
public abstract class MessageEvent extends Event {

    /**
     * The id of the message.
     */
    private final long messageId;

    /**
     * The text channel in which the message was sent.
     */
    private final TextChannel channel;

    /**
     * Creates a new message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api);
        this.messageId = messageId;
        this.channel = channel;
    }

    /**
     * Gets the id of the message.
     *
     * @return The id of the message.
     */
    public long getMessageId() {
        return messageId;
    }

    /**
     * Gets the channel in which the message was sent.
     *
     * @return The channel in which the message was sent.
     */
    public TextChannel getChannel() {
        return channel;
    }

}
