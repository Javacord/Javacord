package de.btobastian.javacord.event.message;

import de.btobastian.javacord.entity.message.Message;

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
