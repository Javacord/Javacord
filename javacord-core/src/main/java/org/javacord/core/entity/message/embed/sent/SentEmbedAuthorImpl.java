package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link SentEmbedAuthor}.
 */
public class SentEmbedAuthorImpl extends SentEmbedMemberImpl<EmbedDraftAuthor, SentEmbedAuthor>
        implements SentEmbedAuthor {

    private final String name;
    private final URL url;
    private final URL iconUrl;
    private final URL proxyIconUrl;

    /**
     * Creates a new embed author.
     *
     * @param data The json data of the author.
     */
    public SentEmbedAuthorImpl(SentEmbed parent, JsonNode data) {
        super(parent, EmbedDraftAuthor.class, SentEmbedAuthor.class);

        name = data.path("name").asText(null);
        try {
            url = data.has("url") ? new URL(data.get("url").asText()) : null;
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
    public String getName() {
        return name;
    }

    @Override
    public Optional<URL> getUrl() {
        return Optional.ofNullable(url);
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
