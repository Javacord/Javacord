package org.javacord.api.event.server.sticker;

import java.util.Optional;

/**
 * A sticker change description event.
 */
public interface StickerChangeDescriptionEvent extends StickerEvent {

    /**
     * Gets the old description of the sticker.
     *
     * @return The old description of the sticker.
     */
    Optional<String> getOldDescription();

    /**
     * Gets the new description of the sticker.
     *
     * @return The new description of the sticker.
     */
    Optional<String> getNewDescription();
}
