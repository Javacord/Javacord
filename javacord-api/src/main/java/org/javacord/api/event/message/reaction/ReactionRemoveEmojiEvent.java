package org.javacord.api.event.message.reaction;

import org.javacord.api.entity.emoji.Emoji;

/**
 * A reaction remove emoji event.
 */
public interface ReactionRemoveEmojiEvent extends ReactionEvent {

    /**
     * Gets the emoji for this event.
     *
     * @return The emoji for this event.
     */
    Emoji getEmoji();
}
