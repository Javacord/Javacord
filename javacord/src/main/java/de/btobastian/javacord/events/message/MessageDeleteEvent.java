package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;

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
