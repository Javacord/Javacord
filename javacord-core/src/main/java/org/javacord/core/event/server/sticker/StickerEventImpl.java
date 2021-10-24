package org.javacord.core.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link StickerEvent}.
 */
public abstract class StickerEventImpl extends ServerEventImpl implements StickerEvent {

    /**
     * The sticker of the event.
     */
    private final Sticker sticker;

    /**
     * Creates a new sticker event.
     *
     * @param sticker The sticker of the event.
     */
    public StickerEventImpl(Sticker sticker) {
        super(sticker.getServer().get());
        this.sticker = sticker;
    }

    @Override
    public Sticker getSticker() {
        return sticker;
    }
}
