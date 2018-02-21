package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiChangeNameEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
