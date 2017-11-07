package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.IconHolder;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAuthor;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link MessageAuthor}.
 */
public class ImplMessageAuthor implements MessageAuthor, IconHolder {

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
    public ImplMessageAuthor(Message message, Long webhookId, JSONObject data) {
        this.message = message;

        this.id = Long.parseLong(data.getString("id"));
        this.name = data.getString("username");
        this.discriminator = data.getString("discriminator");
        this.avatarId = data.getString("avatar");

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
    public String getDiscriminator() {
        return discriminator;
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
    public Optional<URL> getIconUrl() {
        String url = "https://cdn.discordapp.com/embed/avatars/" + Integer.parseInt(discriminator) % 5 + ".png";
        if (avatarId != null) {
            url = "https://cdn.discordapp.com/avatars/" + getId() + "/" + avatarId +
                    (avatarId.startsWith("a_") ? ".gif" : ".png");
        }
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }
}
