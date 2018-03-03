package org.javacord.listener.server.emoji;

import org.javacord.event.server.emoji.CustomEmojiChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.emoji.CustomEmojiChangeNameEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji name changes.
 */
@FunctionalInterface
public interface CustomEmojiChangeNameListener extends ServerAttachableListener, CustomEmojiAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji's name changed.
     *
     * @param event The event.
     */
    void onCustomEmojiChangeName(CustomEmojiChangeNameEvent event);
}
