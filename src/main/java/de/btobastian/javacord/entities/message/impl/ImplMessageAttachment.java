package de.btobastian.javacord.entities.message.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link MessageAttachment}.
 */
public class ImplMessageAttachment implements MessageAttachment {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImplMessageAttachment.class);

    /**
     * The id of the attachment.
     */
    private final long id;

    /**
     * The message of the attachment.
     */
    private final Message message;

    /**
     * The file name of the attachment.
     */
    private final String fileName;

    /**
     * The size of the attachment in bytes.
     */
    private final int size;

    /**
     * The url of the attachment.
     */
    private final String url;

    /**
     * The proxy url of the attachment.
     */
    private final String proxyUrl;

    /**
     * The height of the attachment if it's an image.
     */
    private final Integer height;

    /**
     * The width of the attachment if it's an image.
     */
    private final Integer width;

    /**
     * Creates a new message attachment.
     *
     * @param message The message of the attachment.
     * @param data The data of the attachment.
     */
    public ImplMessageAttachment(Message message, JsonNode data) {
        this.message = message;
        id = data.get("id").asLong();
        fileName = data.get("filename").asText();
        size = data.get("size").asInt();
        url = data.get("url").asText();
        proxyUrl = data.get("proxy_url").asText();
        height = data.has("height") && !data.get("height").isNull() ? data.get("height").asInt() : null;
        width = data.has("width") && !data.get("width").isNull() ? data.get("width").asInt() : null;
    }

    @Override
    public DiscordApi getApi() {
        return message.getApi();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the attachment is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public URL getProxyUrl() {
        try {
            return new URL(proxyUrl);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the proxy url of the attachment is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    @Override
    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("MessageAttachment (file name: %s, url: %s)", getFileName(), getUrl().toString());
    }

}
