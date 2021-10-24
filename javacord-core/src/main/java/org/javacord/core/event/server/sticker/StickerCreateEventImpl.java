package org.javacord.core.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerCreateEvent;

/**
 * The implementation of {@link StickerCreateEvent}.
 */
public class StickerCreateEventImpl extends StickerEventImpl implements StickerCreateEvent {

    /**
     * Creates a new sticker create event.
     *
     * @param sticker The created sticker.
     */
    public StickerCreateEventImpl(Sticker sticker) {
        super(sticker);
    }
}
