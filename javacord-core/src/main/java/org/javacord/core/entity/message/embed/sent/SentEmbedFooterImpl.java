package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;
import org.javacord.core.entity.message.embed.draft.EmbedDraftFooterImpl;

import java.util.Optional;

/**
 * The implementation of {@link SentEmbedFooter}.
 */
public class SentEmbedFooterImpl implements SentEmbedFooter {

    private final SentEmbed parent;
    private final String text;
    private final String iconUrl;
    private final String proxyIconUrl;

    /**
     * Creates a new embed footer.
     *
     * @param data The json data of the footer.
     */
    public SentEmbedFooterImpl(SentEmbed parent, JsonNode data) {
        this.parent = parent;

        text = data.path("text").asText(null);
        iconUrl = data.path("icon_url").asText(null);
        proxyIconUrl = data.path("proxy_icon_url").asText(null);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Optional<String> getIconUrl() {
        return Optional.ofNullable(iconUrl);
    }

    @Override
    public EmbedDraftFooter toEmbedDraftFooter() {
        return new EmbedDraftFooterImpl(parent, this);
    }

    @Override
    public SentEmbed getEmbed() {
        return parent;
    }

    @Override
    public Optional<String> getProxyIconUrl() {
        return Optional.ofNullable(proxyIconUrl);
    }

}
