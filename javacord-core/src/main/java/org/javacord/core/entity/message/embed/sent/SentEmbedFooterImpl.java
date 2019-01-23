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
    private final URL iconUrl;
    private final URL proxyIconUrl;

    /**
     * Creates a new embed footer.
     *
     * @param data The json data of the footer.
     */
    public SentEmbedFooterImpl(SentEmbed parent, JsonNode data) {
        super(parent, EmbedDraftFooter.class, SentEmbedFooter.class);

        text = data.path("text").asText(null);
        try {
            iconUrl = data.has("icon_url") ? new URL(data.get("icon_url").asText()) : null;
            proxyIconUrl = data.has("proxy_icon_url") ? new URL(data.get("proxy_icon_url").asText()) : null;
        } catch (MalformedURLException e) {
            /*
            If any URL cannot be parsed, we have reaced an unreachable state, as the URL fields are
            OPTIONAL but not NULLABLE. We can assert that URLs coming from Discord are always valid.
            */
            throw new AssertionError("The URL recieved from discord is invalid!", e);
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Optional<URL> getIconUrl() {
        return Optional.ofNullable(iconUrl);
    }

    @Override
    public Optional<URL> getProxyIconUrl() {
        return Optional.ofNullable(proxyIconUrl);
    }

}
