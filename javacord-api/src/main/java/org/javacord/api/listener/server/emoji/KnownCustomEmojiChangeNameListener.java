package org.javacord.api.listener.server.emoji;

import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeNameEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji name changes.
 */
@FunctionalInterface
public interface KnownCustomEmojiChangeNameListener extends ServerAttachableListener,
                                                            KnownCustomEmojiAttachableListener,
                                                            GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji's name changed.
     *
     * @param event The event.
     */
    void onKnownCustomEmojiChangeName(KnownCustomEmojiChangeNameEvent event);
}
