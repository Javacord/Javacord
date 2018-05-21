package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeNameEvent;

/**
 * The implementation of {@link KnownCustomEmojiChangeNameEvent}.
 */
public class KnownCustomEmojiChangeNameEventImpl extends KnownCustomEmojiEventImpl
        implements KnownCustomEmojiChangeNameEvent {

    /**
     * The new name of the custom emoji.
     */
    private final String newName;

    /**
     * The old name of the custom emoji.
     */
    private final String oldName;

    /**
     * Creates a new custom emoji change name event.
     *
     * @param emoji The updated emoji.
     * @param newName The new name of the custom emoji.
     * @param oldName The old name of the custom emoji.
     */
    public KnownCustomEmojiChangeNameEventImpl(KnownCustomEmoji emoji, String newName, String oldName) {
        super(emoji);
        this.newName = newName;
        this.oldName = oldName;
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
