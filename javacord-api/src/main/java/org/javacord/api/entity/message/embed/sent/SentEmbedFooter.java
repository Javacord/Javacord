package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedFooter;

import java.util.Optional;

public interface SentEmbedFooter extends BaseEmbedFooter {

    @Override
    SentEmbed getEmbed();

    /**
     * Gets the proxy url of the footer icon.
     *
     * @return The proxy url of the footer icon.
     */
    Optional<String> getProxyIconUrl();

}
