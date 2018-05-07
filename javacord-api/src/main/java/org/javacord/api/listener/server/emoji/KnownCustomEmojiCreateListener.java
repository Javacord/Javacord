package org.javacord.api.listener.server.emoji;

import org.javacord.api.event.server.emoji.KnownCustomEmojiCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji create event.
 */
@FunctionalInterface
public interface KnownCustomEmojiCreateListener extends ServerAttachableListener, GloballyAttachableListener,
                                                        ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji is created.
     *
     * @param event The event.
     */
    void onKnownCustomEmojiCreate(KnownCustomEmojiCreateEvent event);
}
