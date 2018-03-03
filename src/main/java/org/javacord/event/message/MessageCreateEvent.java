package org.javacord.event.message;

import org.javacord.DiscordApi;
import org.javacord.entity.message.Message;

/**
 * A message create event.
 */
public class MessageCreateEvent extends CertainMessageEvent {

    /**
     * Creates a new event instance.
     *
     * @param api The discord api instance.
     * @param message The created message.
     */
    public MessageCreateEvent(DiscordApi api, Message message) {
        super(api, message);
    }

}
