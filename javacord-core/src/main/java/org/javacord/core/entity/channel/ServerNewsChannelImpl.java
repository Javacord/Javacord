package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.FollowedChannel;
import org.javacord.api.entity.channel.ServerNewsChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.news.InternalServerNewsChannelAttachableListenerManager;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

public class ServerNewsChannelImpl extends ServerMessageChannelImpl implements ServerNewsChannel,
        InternalServerNewsChannelAttachableListenerManager {

    /**
     * Creates a new server message channel.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public ServerNewsChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
    }

    @Override
    public CompletableFuture<FollowedChannel> followChannel(long webhookChannelId) {
        return new RestRequest<FollowedChannel>(getApi(), RestMethod.POST, RestEndpoint.FOLLOW_NEWS_CHANNEL)
                .setUrlParameters(getIdAsString())
                .addQueryParameter("webhook_channel_id", String.valueOf(webhookChannelId))
                .execute(result -> new FollowedChannelImpl(result.getJsonBody()));
    }
}
