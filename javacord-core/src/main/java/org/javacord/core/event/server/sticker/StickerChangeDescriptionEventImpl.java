package org.javacord.core.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerChangeDescriptionEvent;

import java.util.Optional;

/**
 * The implementation of {@link StickerChangeDescriptionEvent}.
 */
public class StickerChangeDescriptionEventImpl extends StickerEventImpl implements StickerChangeDescriptionEvent {

    /**
     * The old description of the sticker.
     */
    private final String oldDescription;

    /**
     * The new description of the sticker.
     */
    private final String newDescription;

    /**
     * Creates a new sticker change description event.
     *
     * @param sticker        The updated sticker.
     * @param oldDescription The old description of the sticker. Might be null if it wasn't set.
     * @param newDescription The new description of the sticker. Might be null if it were removed.
     */
    public StickerChangeDescriptionEventImpl(Sticker sticker, String oldDescription, String newDescription) {
        super(sticker);

        this.oldDescription = oldDescription;
        this.newDescription = newDescription;
    }

    @Override
    public Optional<String> getOldDescription() {
        return Optional.ofNullable(oldDescription);
    }

    @Override
    public Optional<String> getNewDescription() {
        return Optional.ofNullable(newDescription);
    }
}
