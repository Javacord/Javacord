package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedAuthor;

import java.util.Optional;

public interface SentEmbedAuthor extends BaseEmbedAuthor {

    @Override
    SentEmbed getEmbed();

    /**
     * Gets the proxy url of the author icon.
     *
     * @return The proxy url of the author icon.
     */
    Optional<String> getProxyIconUrl();

}
