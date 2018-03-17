package org.javacord.core.event.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CachedMessagePinEvent;

/**
 * The implementation of {@link CachedMessagePinEvent}.
 */
public class CachedMessagePinEventImpl extends CertainMessageEventImpl implements CachedMessagePinEvent {

    /**
     * Creates a new cached message pin event.
     *
     * @param message The message.
     */
    public CachedMessagePinEventImpl(Message message) {
        super(message);
    }

}
