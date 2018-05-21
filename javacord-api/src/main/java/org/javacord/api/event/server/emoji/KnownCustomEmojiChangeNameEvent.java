package org.javacord.api.event.server.emoji;

/**
 * A custom emoji change name event.
 */
public interface KnownCustomEmojiChangeNameEvent extends KnownCustomEmojiEvent {

    /**
     * Gets the old name of the custom emoji.
     *
     * @return The old name of the custom emoji.
     */
    String getOldName();

    /**
     * Gets the new name of the custom emoji.
     *
     * @return The new name of the custom emoji.
     */
    String getNewName();

}
