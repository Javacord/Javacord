package org.javacord.core.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerDeleteEvent;

/**
 * The implementation of {@link StickerDeleteEvent}.
 */
public class StickerDeleteEventImpl extends StickerEventImpl implements StickerDeleteEvent {

    /**
     * Creates a new sticker delete event.
     *
     * @param sticker The deleted sticker.
     */
    public StickerDeleteEventImpl(Sticker sticker) {
        super(sticker);
    }
}
