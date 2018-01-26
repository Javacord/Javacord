package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiChangeNameEvent;

/**
 * This listener listens to custom emoji name changes.
 */
@FunctionalInterface
public interface CustomEmojiChangeNameListener {

    /**
     * This method is called every time a custom emoji's name changed.
     *
     * @param event The event.
     */
    void onCustomEmojiChangeName(CustomEmojiChangeNameEvent event);
}
