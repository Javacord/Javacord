package de.btobastian.javacord.entities.message.embed.impl;

import de.btobastian.javacord.entities.message.embed.EmbedProvider;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of {@link EmbedProvider}.
 */
public class ImplEmbedProvider implements EmbedProvider {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbedProvider.class);

    private String name;
    private String url;

    /**
     * Creates a new embed provider.
     *
     * @param data The json data of the provider.
     */
    public ImplEmbedProvider(JSONObject data) {
        name = data.has("name") ? data.getString("name") : null;
        url = data.has("url") && !data.isNull("url") ? data.getString("url") : null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public URL getUrl() {
        if (url == null) {
            return null;
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed provider is malformed! Please contact the developer!", e);
            return null;
        }
    }
}