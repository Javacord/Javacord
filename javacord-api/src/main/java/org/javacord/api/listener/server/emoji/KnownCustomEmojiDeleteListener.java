package org.javacord.api.listener.server.emoji;

import org.javacord.api.event.server.emoji.KnownCustomEmojiDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji delete event.
 */
@FunctionalInterface
public interface KnownCustomEmojiDeleteListener extends ServerAttachableListener, KnownCustomEmojiAttachableListener,
                                                        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji is deleted.
     *
     * @param event The event.
     */
    void onKnownCustomEmojiDelete(KnownCustomEmojiDeleteEvent event);
}
