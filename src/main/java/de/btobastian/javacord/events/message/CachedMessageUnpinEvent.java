package de.btobastian.javacord.events.message;

import de.btobastian.javacord.entities.message.Message;

/**
 * A cached message unpin event.
 */
public class CachedMessageUnpinEvent extends CertainMessageEvent {

    /**
     * Creates a new cached message unpin event.
     *
     * @param message The message.
     */
    public CachedMessageUnpinEvent(Message message) {
        super(message.getApi(), message);
    }

}
