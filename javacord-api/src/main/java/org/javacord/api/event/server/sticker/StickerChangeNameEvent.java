package org.javacord.api.event.server.sticker;

/**
 * A sticker change name event.
 */
public interface StickerChangeNameEvent extends StickerEvent {

    /**
     * Gets the old name of the sticker.
     *
     * @return The old name of the sticker.
     */
    String getOldName();

    /**
     * Gets the new name of the sticker.
     *
     * @return The new name of the sticker.
     */
    String getNewName();
}
