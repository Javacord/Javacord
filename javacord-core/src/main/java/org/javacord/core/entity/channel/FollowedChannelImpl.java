package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.FollowedChannel;

public class FollowedChannelImpl implements FollowedChannel {

    /**
     * The channel id of the channel.
     */
    private final long channelId;

    /**
     * The targeted webhook id of the channel.
     */
    private final long targetedWebhookId;

    /**
     * Creates a new followed channel.
     *
     * @param jsonNode The json node of the followed channel.
     */
    public FollowedChannelImpl(JsonNode jsonNode) {
        channelId = jsonNode.get("channel_id").asLong();
        targetedWebhookId = jsonNode.get("webhook_id").asLong();
    }

    @Override
    public long getChannelId() {
        return channelId;
    }

    @Override
    public long getTargetedWebhookId() {
        return targetedWebhookId;
    }
}
