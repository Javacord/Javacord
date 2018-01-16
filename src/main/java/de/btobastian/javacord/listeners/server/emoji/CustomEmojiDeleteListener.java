package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiDeleteEvent;

/**
 * This listener listens to custom emoji delete event.
 */
@FunctionalInterface
public interface CustomEmojiDeleteListener {

    /**
     * This method is called every time a custom emoji is deleted.
     *
     * @param event The event.
     */
    void onCustomEmojiDelete(CustomEmojiDeleteEvent event);
}
