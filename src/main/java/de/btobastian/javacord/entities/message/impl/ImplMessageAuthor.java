package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.MessageAuthor;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

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
    public ImplMessageAuthor(Message message, Long webhookId, JSONObject data) {
        this.message = message;

        this.id = Long.parseLong(data.getString("id"));
        this.name = data.getString("username");
        this.discriminator = data.getString("discriminator");
        this.avatarId = data.has("avatar") && !data.isNull("avatar") ? data.getString("avatar") : null;

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
    public Icon getAvatar() {
        String url = "https://cdn.discordapp.com/embed/avatars/" + Integer.parseInt(discriminator) % 5 + ".png";
        if (avatarId != null) {
            url = "https://cdn.discordapp.com/avatars/" + getId() + "/" + avatarId +
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

}
