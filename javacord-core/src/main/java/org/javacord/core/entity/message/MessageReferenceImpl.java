package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.MessageReference;

import java.util.Optional;

/**
 * The implementation of {@link MessageReference}.
 */
public class MessageReferenceImpl implements MessageReference {

    /**
     * Id of the originating message.
     */
    private final Long messageId;
    /**
     * Id of the originating message's channel.
     */
    private final Long channelId;
    /**
     * Id of the originating message's server.
     */
    private final Long serverId;

    /**
     * Creates a new message reference object.
     *
     * @param data The json data of the message reference.
     */
    public MessageReferenceImpl(JsonNode data) {
        System.out.println(data);
        messageId = data.hasNonNull("message_id") ? data.get("message_id").asLong() : null;
        channelId = data.hasNonNull("channel_id") ? data.get("channel_id").asLong() : null;
        serverId = data.hasNonNull("guild_id") ? data.get("guild_id").asLong() : null;
    }

    @Override
    public Optional<Long> getMessageId() {
        return Optional.ofNullable(messageId);
    }

    @Override
    public Optional<Long> getChannelId() {
        return Optional.ofNullable(channelId);
    }

    @Override
    public Optional<Long> getServerId() {
        return Optional.ofNullable(serverId);
    }
}
