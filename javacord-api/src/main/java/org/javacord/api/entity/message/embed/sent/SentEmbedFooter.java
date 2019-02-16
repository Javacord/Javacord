package org.javacord.api.entity.message.embed.sent;

import java.util.Optional;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;

public interface SentEmbedFooter extends BaseEmbedFooter, SentEmbedMember<EmbedDraftFooter, SentEmbedFooter> {

    /**
     * Gets the proxy url of the footer icon.
     *
     * @return The proxy url of the footer icon.
     */
    Optional<String> getProxyIconUrl();
}
