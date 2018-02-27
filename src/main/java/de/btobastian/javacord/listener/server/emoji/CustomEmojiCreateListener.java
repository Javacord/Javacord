package de.btobastian.javacord.listener.server.emoji;

import de.btobastian.javacord.event.server.emoji.CustomEmojiCreateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
