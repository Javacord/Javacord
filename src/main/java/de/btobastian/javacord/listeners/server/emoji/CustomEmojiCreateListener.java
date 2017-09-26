package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiCreateEvent;

/**
 * This listener listens to custom emoji create event.
 */
@FunctionalInterface
public interface CustomEmojiCreateListener {

    /**
     * This method is called every a custom emoji is created.
     *
     * @param event The event.
     */
    void onCustomEmojiCreate(CustomEmojiCreateEvent event);
}
