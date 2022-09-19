package org.javacord.core.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerChangeNameEvent;

/**
 * The implementation of {@link StickerChangeNameEvent}.
 */
public class StickerChangeNameEventImpl extends StickerEventImpl implements StickerChangeNameEvent {

    /**
     * The old name of the sticker.
     */
    private final String oldName;

    /**
     * The new name of the sticker.
     */
    private final String newName;

    /**
     * Creates a new sticker change name event.
     *
     * @param sticker The updated sticker.
     * @param oldName The old name of the sticker.
     * @param newName The new name of the sticker.
     */
    public StickerChangeNameEventImpl(Sticker sticker, String oldName, String newName) {
        super(sticker);

        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }

    @Override
    public String getNewName() {
        return newName;
    }
}
