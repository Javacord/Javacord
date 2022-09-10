package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageReference;
import org.javacord.core.DiscordApiImpl;

import java.util.Optional;

public class MessageReferenceImpl implements MessageReference {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The server id of the message reference. Can be null.
     */
    private final Long serverId;

    /**
     * The channel id of the message reference.
     */
    private final long channelId;

    /**
     * The message id of the message reference. Can be null.
     */
    private final Long messageId;

    /**
     * The referenced message. Can be null.
     */
    private final Message message;

    /**
     * Creates a new message reference object.
     *
     * @param api The discord api instance.
     * @param message The message that got referenced. Can be null.
     * @param data The json data of the message reference.
     */
    public MessageReferenceImpl(DiscordApiImpl api, Message message, JsonNode data) {
        this.api = api;
        if (data.hasNonNull("guild_id")) {
            this.serverId = data.get("guild_id").asLong();
        } else {
            this.serverId = null;
        }
        this.channelId = data.get("channel_id").asLong();

        if (data.hasNonNull("message_id")) {
            this.messageId = data.get("message_id").asLong();
        } else {
            this.messageId = null;
        }

        this.message = message;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public Optional<Long> getServerId() {
        return Optional.ofNullable(serverId);
    }

    @Override
    public long getChannelId() {
        return channelId;
    }

    @Override
    public Optional<Long> getMessageId() {
        return Optional.ofNullable(messageId);
    }

    @Override
    public Optional<Message> getMessage() {
        return Optional.ofNullable(message);
    }
}
