package de.btobastian.javacord.listener.server.emoji;

import de.btobastian.javacord.event.server.emoji.CustomEmojiDeleteEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
