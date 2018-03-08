package org.javacord.event.server.emoji;

/**
 * A custom emoji change name event.
 */
public interface CustomEmojiChangeNameEvent extends CustomEmojiEvent {

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
