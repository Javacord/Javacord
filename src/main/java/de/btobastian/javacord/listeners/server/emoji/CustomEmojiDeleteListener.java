package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiDeleteEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
