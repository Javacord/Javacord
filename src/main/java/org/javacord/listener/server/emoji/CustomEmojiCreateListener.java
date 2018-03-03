package org.javacord.listener.server.emoji;

import org.javacord.event.server.emoji.CustomEmojiCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.emoji.CustomEmojiCreateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji create event.
 */
@FunctionalInterface
public interface CustomEmojiCreateListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji is created.
     *
     * @param event The event.
     */
    void onCustomEmojiCreate(CustomEmojiCreateEvent event);
}
