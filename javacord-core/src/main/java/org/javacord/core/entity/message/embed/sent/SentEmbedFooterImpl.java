package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.draft.EmbedDraftFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link SentEmbedFooter}.
 */
public class SentEmbedFooterImpl extends SentEmbedMemberImpl<EmbedDraftFooter, SentEmbedFooter> implements SentEmbedFooter {

    private final String text;
    private final String iconUrl;
    private final String proxyIconUrl;

    /**
     * Creates a new embed footer.
     *
     * @param data The json data of the footer.
     */
    public SentEmbedFooterImpl(SentEmbed parent, JsonNode data) {
        super(parent, EmbedDraftFooter.class, SentEmbedFooter.class);

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
    public Optional<String> getProxyIconUrl() {
        return Optional.ofNullable(proxyIconUrl);
    }

}
