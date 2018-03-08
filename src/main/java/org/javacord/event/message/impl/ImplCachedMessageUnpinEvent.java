package org.javacord.event.message.impl;

import org.javacord.entity.message.Message;
import org.javacord.event.message.CachedMessageUnpinEvent;

/**
 * The implementation of {@link CachedMessageUnpinEvent}.
 */
public class ImplCachedMessageUnpinEvent extends ImplCertainMessageEvent implements CachedMessageUnpinEvent {

    /**
     * Creates a new cached message unpin event.
     *
     * @param message The message.
     */
    public ImplCachedMessageUnpinEvent(Message message) {
        super(message);
    }

}
