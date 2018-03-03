package org.javacord.entity.message.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Icon;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.MessageAuthor;
import org.javacord.util.logging.LoggerUtil;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.Icon;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.MessageAuthor;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link MessageAuthor}.
 */
public class ImplMessageAuthor implements MessageAuthor {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImplMessageAuthor.class);

    private final Message message;

    private final Long webhookId;

    private final long id;
    private final String name;
    private final String discriminator;
    private final String avatarId;

    /**
     * Creates a new message author.
     *
     * @param message The message.
     * @param webhookId The id of the webhook, if the author is a webhook.
     * @param data The json data of the author.
     */
    public ImplMessageAuthor(Message message, Long webhookId, JsonNode data) {
        this.message = message;

        this.id = data.get("id").asLong();
        this.name = data.get("username").asText();
        this.discriminator = data.get("discriminator").asText();
        this.avatarId = data.has("avatar") && !data.get("avatar").isNull() ? data.get("avatar").asText() : null;

        this.webhookId = webhookId;
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
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getDiscriminator() {
        return isUser() ? Optional.of(discriminator) : Optional.empty();
    }

    @Override
    public Icon getAvatar() {
        String url = "https://cdn.discordapp.com/embed/avatars/" + Integer.parseInt(discriminator) % 5 + ".png";
        if (avatarId != null) {
            url = "https://cdn.discordapp.com/avatars/" + getIdAsString() + "/" + avatarId +
                    (avatarId.startsWith("a_") ? ".gif" : ".png");
        }
        try {
            return new ImplIcon(getApi(), new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public boolean isUser() {
        return webhookId == null;
    }

    @Override
    public boolean isWebhook() {
        return webhookId != null;
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

    public String toString() {
        return String.format("MessageAuthor (id: %s, name: %s)", getIdAsString(), getName());
    }

}
