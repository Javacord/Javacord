package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.user.UserImpl;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link MessageAuthor}.
 */
public class MessageAuthorImpl implements MessageAuthor {

    private final Message message;

    private final UserImpl user;

    private final Long webhookId;

    private final long id;
    private final String name;
    private final String discriminator;
    private final String avatarId;

    /**
     * Creates a new message author.
     *
     * @param message     The message.
     * @param webhookId   The id of the webhook, if the author is a webhook.
     * @param messageJson The json data of the author.
     */
    public MessageAuthorImpl(Message message, Long webhookId, JsonNode messageJson) {
        this.message = message;

        JsonNode authorJson = messageJson.get("author");
        id = authorJson.get("id").asLong();
        name = authorJson.get("username").asText();
        discriminator = authorJson.get("discriminator").asText();
        avatarId = authorJson.has("avatar") && !authorJson.get("avatar").isNull()
                ? authorJson.get("avatar").asText() : null;

        user = webhookId == null
                ? new UserImpl((DiscordApiImpl) getApi(), authorJson)
                : null;

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
    public Optional<Long> getWebhookId() {
        return Optional.ofNullable(webhookId);
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
        return UserImpl.getAvatar(message.getApi(), avatarId, discriminator, id);
    }

    @Override
    public Icon getAvatar(int size) {
        return UserImpl.getAvatar(message.getApi(), avatarId, discriminator, id, size);
    }

    @Override
    public boolean isUser() {
        return webhookId == null;
    }

    @Override
    public Optional<User> asUser() {
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<Member> asMember() {
        return getMessage().getServer().flatMap(server -> server.getMemberById(getId()));
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

    @Override
    public String toString() {
        return String.format("MessageAuthor (id: %s, name: %s)", getIdAsString(), getName());
    }

}
