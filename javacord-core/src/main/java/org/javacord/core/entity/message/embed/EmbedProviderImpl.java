package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedProvider;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link EmbedProvider}.
 */
public class EmbedProviderImpl implements EmbedProvider {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedProviderImpl.class);

    private final String name;
    private final String url;

    /**
     * Creates a new embed provider.
     *
     * @param data The json data of the provider.
     */
    public EmbedProviderImpl(JsonNode data) {
        name = data.has("name") ? data.get("name").asText() : null;
        url = data.hasNonNull("url") ? data.get("url").asText() : null;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<URL> getUrl() {
        if (url == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed provider is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }
}