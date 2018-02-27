package de.btobastian.javacord.event.server.emoji;

import de.btobastian.javacord.entity.emoji.KnownCustomEmoji;

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
