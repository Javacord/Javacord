package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.message.Message;

/**
 * A message event where the message is guaranteed to be in the cache.
 */
public abstract class CertainMessageEvent extends MessageEvent {

    /**
     * The message of the event.
     */
    private final Message message;

    /**
     * Creates a new certain message event.
     *
     * @param api The discord api instance.
     * @param message The message.
     */
    public CertainMessageEvent(DiscordApi api, Message message) {
        super(api, message.getId(), message.getChannel());
        this.message = message;
    }

    /**
     * Gets the message of the event.
     *
     * @return The message of the event.
     */
    public Message getMessage() {
        return message;
    }
    
}
