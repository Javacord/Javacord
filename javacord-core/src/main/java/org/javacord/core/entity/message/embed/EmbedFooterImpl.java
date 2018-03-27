package org.javacord.core.entity.message.embed;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.embed.EmbedFooter;
import org.javacord.core.util.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link EmbedFooter}.
 */
public class EmbedFooterImpl implements EmbedFooter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedFooterImpl.class);

    private String text;
    private String iconUrl;
    private String proxyIconUrl;

    /**
     * Creates a new embed footer.
     *
     * @param data The json data of the footer.
     */
    public EmbedFooterImpl(JsonNode data) {
        text = data.has("text") ? data.get("text").asText() : null;
        iconUrl = data.has("icon_url") && !data.get("icon_url").isNull() ? data.get("icon_url").asText() : null;
        proxyIconUrl = data.has("proxy_icon_url") && !data.get("proxy_icon_url").isNull()
                ? data.get("proxy_icon_url").asText() : null;
    }

    @Override
    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    @Override
    public Optional<URL> getIconUrl() {
        if (iconUrl == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(iconUrl));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the icon url of the embed footer is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<URL> getProxyIconUrl() {
        if (proxyIconUrl == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(proxyIconUrl));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the proxy icon url of the embed footer is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

}