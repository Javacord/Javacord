package org.javacord.api.entity.message.embed.sent;

import java.util.OptionalInt;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;

interface SentEmbedImageMember<D extends EmbedDraftMember, S extends SentEmbedMember> extends SentEmbedMember<D, S> {

    /**
     * Gets the height of the image.
     *
     * @return The height of the image.
     */
    OptionalInt getHeight();

    /**
     * Gets the width of the image.
     *
     * @return The width of the image.
     */
    OptionalInt getWidth();

}
