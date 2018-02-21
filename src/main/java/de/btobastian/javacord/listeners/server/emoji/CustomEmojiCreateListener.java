package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiCreateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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
