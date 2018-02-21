package de.btobastian.javacord.events.server.emoji;

import de.btobastian.javacord.entities.message.emoji.KnownCustomEmoji;

/**
 * A custom emoji create event.
 */
public class CustomEmojiCreateEvent extends CustomEmojiEvent {

    /**
     * Creates a new custom emoji create event.
     *
     * @param emoji The created emoji.
     */
    public CustomEmojiCreateEvent(KnownCustomEmoji emoji) {
        super(emoji);
    }

}
