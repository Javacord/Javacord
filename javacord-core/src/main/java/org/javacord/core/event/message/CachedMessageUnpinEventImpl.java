package org.javacord.core.event.message;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.CachedMessageUnpinEvent;

/**
 * The implementation of {@link CachedMessageUnpinEvent}.
 */
public class CachedMessageUnpinEventImpl extends CertainMessageEventImpl implements CachedMessageUnpinEvent {

    /**
     * Creates a new cached message unpin event.
     *
     * @param message The message.
     */
    public CachedMessageUnpinEventImpl(Message message) {
        super(message);
    }

}
