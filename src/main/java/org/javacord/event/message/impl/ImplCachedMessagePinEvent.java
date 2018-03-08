package org.javacord.event.message.impl;

import org.javacord.entity.message.Message;
import org.javacord.event.message.CachedMessagePinEvent;

/**
 * The implementation of {@link CachedMessagePinEvent}.
 */
public class ImplCachedMessagePinEvent extends ImplCertainMessageEvent implements CachedMessagePinEvent {

    /**
     * Creates a new cached message pin event.
     *
     * @param message The message.
     */
    public ImplCachedMessagePinEvent(Message message) {
        super(message);
    }

}
