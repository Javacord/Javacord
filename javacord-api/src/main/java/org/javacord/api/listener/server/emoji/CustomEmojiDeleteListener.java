package org.javacord.api.listener.server.emoji;

import org.javacord.api.event.server.emoji.CustomEmojiDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji delete event.
 */
@FunctionalInterface
public interface CustomEmojiDeleteListener extends ServerAttachableListener, CustomEmojiAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji is deleted.
     *
     * @param event The event.
     */
    void onCustomEmojiDelete(CustomEmojiDeleteEvent event);
}
