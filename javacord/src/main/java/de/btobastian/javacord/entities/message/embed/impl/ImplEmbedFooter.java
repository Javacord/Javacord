package de.btobastian.javacord.entities.message.embed.impl;

import de.btobastian.javacord.entities.message.embed.EmbedFooter;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link EmbedFooter}.
 */
public class ImplEmbedFooter implements EmbedFooter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbedFooter.class);

    private String text;
    private String iconUrl;
    private String proxyIconUrl;

    /**
     * Creates a new embed footer.
     *
     * @param data The json data of the footer.
     */
    public ImplEmbedFooter(JSONObject data) {
        text = data.has("text") ? data.getString("text") : null;
        iconUrl = data.has("icon_url") && !data.isNull("icon_url") ? data.getString("icon_url") : null;
        proxyIconUrl = data.has("proxy_icon_url") && !data.isNull("proxy_icon_url")
                ? data.getString("proxy_icon_url") : null;
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