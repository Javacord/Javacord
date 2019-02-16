package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;

public interface SentEmbedVideo extends MissingEmbedDraftMember<SentEmbedVideo>,
        SentEmbedImageMember<EmbedDraftMember, SentEmbedVideo> {

    /**
     * Gets the url of the video.
     *
     * @return The url of the video.
     */
    String getUrl();

}
