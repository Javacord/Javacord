package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedProvider;

/**
 * The implementation of {@link SentEmbedProvider}.
 */
public class SentEmbedProviderImpl implements SentEmbedProvider {

    private final SentEmbed parent;
    private final String name;
    private final String url;

    /**
     * Creates a new embed provider.
     *
     * @param data The json data of the provider.
     */
    public SentEmbedProviderImpl(SentEmbed parent, JsonNode data) {
        this.parent = parent;

        name = data.has("name") ? data.get("name").asText() : null;
        url = data.path("url").asText(null);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        assert url != null : "Discord didn't send a URL!";
        return url;
    }

    @Override
    public Optional<SentEmbed> getEmbed() {
        return Optional.ofNullable(parent);
    }
}