package de.btobastian.javacord.listener.server.emoji;

import de.btobastian.javacord.event.server.emoji.CustomEmojiChangeNameEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
