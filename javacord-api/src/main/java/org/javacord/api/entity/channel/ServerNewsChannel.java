package org.javacord.api.entity.channel;

import org.javacord.api.listener.channel.server.news.ServerNewsChannelAttachableListenerManager;

import java.util.concurrent.CompletableFuture;

public interface ServerNewsChannel extends ServerMessageChannel, ServerNewsChannelAttachableListenerManager {
    /**
     * Gets the type of channel.
     *
     * @return The type of channel.
     */
    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_NEWS_CHANNEL;
    }

    /**
     * Used follow a news Channel, so you are able to receive the messages in another server\channel.
     *
     * @param webhookChannelId The channel id of the webhook.
     * @return A future to check if the action was successful.
     */
    CompletableFuture<FollowedChannel> followChannel(long webhookChannelId);

    /**
     * Used follow a news Channel, so you are able to receive the messages in another server\channel.
     *
     * @param webhookChannelId The channel id of the webhook.
     * @return A future to check if the action was successful.
     */
    default CompletableFuture<FollowedChannel> followChannel(String webhookChannelId) {
        try {
            return followChannel(Long.parseLong(webhookChannelId));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given channel id is not a valid long value!", e);
        }
    }
}

