package org.javacord.api.event.server.sticker;

/**
 * A sticker change tags event.
 */
public interface StickerChangeTagsEvent extends StickerEvent {

    /**
     * Gets the old tags of the sticker.
     *
     * @return The old tags of the sticker.
     */
    String getOldTags();

    /**
     * Gets the new tags of the sticker.
     *
     * @return The new tags of the sticker.
     */
    String getNewTags();
}
