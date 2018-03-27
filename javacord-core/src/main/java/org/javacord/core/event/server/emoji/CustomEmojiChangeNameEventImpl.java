package org.javacord.core.event.server.emoji;

import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.event.server.emoji.CustomEmojiChangeNameEvent;

/**
 * The implementation of {@link CustomEmojiChangeNameEvent}.
 */
public class CustomEmojiChangeNameEventImpl extends CustomEmojiEventImpl implements CustomEmojiChangeNameEvent {

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
    public CustomEmojiChangeNameEventImpl(KnownCustomEmoji emoji, String newName, String oldName) {
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
