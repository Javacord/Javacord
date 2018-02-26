package de.btobastian.javacord.events.message;

import de.btobastian.javacord.entities.message.Message;

/**
 * A cached message pin event.
 */
public class CachedMessagePinEvent extends CertainMessageEvent {

    /**
     * Creates a new cached message pin event.
     *
     * @param message The message.
     */
    public CachedMessagePinEvent(Message message) {
        super(message.getApi(), message);
    }

}
