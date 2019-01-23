package org.javacord.core.entity.message.embed.sent;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.sent.SentEmbedProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link SentEmbedProvider}.
 */
public class SentEmbedProviderImpl implements SentEmbedProvider {

    private final SentEmbed parent;
    private final String name;
    private final URL url;

    /**
     * Creates a new embed provider.
     *
     * @param data The json data of the provider.
     */
    public SentEmbedProviderImpl(SentEmbed parent, JsonNode data) {
        this.parent = parent;

        name = data.has("name") ? data.get("name").asText() : null;
        try {
            url = data.has("url") ? new URL(data.get("url").asText()) : null;
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
    public URL getUrl() {
        assert url != null : "Discord didn't send a URL!";
        return url;
    }

    @Override
    public Optional<SentEmbed> getEmbed() {
        return Optional.ofNullable(parent);
    }
}