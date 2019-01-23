package org.javacord.api.entity.message.embed;

import java.net.URL;
import java.util.Optional;

import org.javacord.api.entity.message.embed.draft.EmbedDraftThumbnail;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;
import org.javacord.api.util.Specializable;

public interface BaseEmbedThumbnail extends Specializable<BaseEmbedThumbnail> {

    /**
     * Gets the url of the thumbnail.
     *
     * @return The url of the thumbnail.
     */
    URL getUrl();

}
