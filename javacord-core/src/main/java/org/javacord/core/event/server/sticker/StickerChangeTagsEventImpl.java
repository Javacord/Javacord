package org.javacord.core.event.server.sticker;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.event.server.sticker.StickerChangeTagsEvent;

/**
 * The implementation of {@link StickerChangeTagsEvent}.
 */
public class StickerChangeTagsEventImpl extends StickerEventImpl implements StickerChangeTagsEvent {

    /**
     * The old tags of the sticker.
     */
    private final String oldTags;

    /**
     * The new tags of the sticker.
     */
    private final String newTags;

    /**
     * Creates a new sticker change tags event.
     *
     * @param sticker The updated sticker.
     * @param oldTags The old tags of the sticker.
     * @param newTags The new tags of the sticker.
     */
    public StickerChangeTagsEventImpl(Sticker sticker, String oldTags, String newTags) {
        super(sticker);

        this.oldTags = oldTags;
        this.newTags = newTags;
    }

    @Override
    public String getOldTags() {
        return oldTags;
    }

    @Override
    public String getNewTags() {
        return newTags;
    }
}
