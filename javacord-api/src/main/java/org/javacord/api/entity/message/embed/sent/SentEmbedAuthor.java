package org.javacord.api.entity.message.embed.sent;

import java.net.URL;
import java.util.Optional;

import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;

public interface SentEmbedAuthor extends BaseEmbedAuthor, SentEmbedMember<EmbedDraftAuthor, SentEmbedAuthor> {

    /**
     * Gets the proxy url of the author icon.
     *
     * @return The proxy url of the author icon.
     */
    Optional<URL> getProxyIconUrl();

}
