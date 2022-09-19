package org.javacord.api.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.ServerEvent;

/**
 * A sticker event.
 */
public interface StickerEvent extends ServerEvent {

    /**
     * Gets the sticker of the event.
     *
     * @return The sticker of the event.
     */
    Sticker getSticker();
}
