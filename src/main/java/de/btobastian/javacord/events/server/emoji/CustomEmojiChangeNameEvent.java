package de.btobastian.javacord.events.server.emoji;

import de.btobastian.javacord.entities.message.emoji.CustomEmoji;

/**
 * A custom emoji change name event.
 */
public class CustomEmojiChangeNameEvent extends CustomEmojiEvent {

    /**
     * The new name of the custom emoji.
     */
    private final String newName;

    /**
     * The old name of the custom emoji.
     */
    private final String oldName;

    /**
     * Creates a new custom emoji change name event.
     *
     * @param emoji The updated emoji.
     * @param newName The new name of the custom emoji.
     * @param oldName The old name of the custom emoji.
     */
    public CustomEmojiChangeNameEvent(CustomEmoji emoji, String newName, String oldName) {
        super(emoji);
        this.newName = newName;
        this.oldName = oldName;
    }

    /**
     * Gets the old name of the custom emoji.
     *
     * @return The old name of the custom emoji.
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Gets the new name of the custom emoji.
     *
     * @return The new name of the custom emoji.
     */
    public String getNewName() {
        return newName;
    }

}
