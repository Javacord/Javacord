package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.internal.ServerForumChannelBuilderDelegate;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerForumChannelBuilderDelegate}.
 */
public class ServerForumChannelBuilderDelegateImpl extends RegularServerChannelBuilderDelegateImpl
        implements ServerForumChannelBuilderDelegate {

    /**
     * The category of the channel.
     */
    private ChannelCategory category = null;

    /**
     * Creates a new server text channel builder delegate.
     *
     * @param server The server of the server text channel.
     */
    public ServerForumChannelBuilderDelegateImpl(ServerImpl server) {
        super(server);
    }

    @Override
    public void setCategory(ChannelCategory category) {
        this.category = category;
    }

    @Override
    public CompletableFuture<ServerForumChannel> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("type", ChannelType.SERVER_FORUM_CHANNEL.getId());
        super.prepareBody(body);

        if (category != null) {
            body.put("parent_id", category.getIdAsString());
        }

        return new RestRequest<ServerForumChannel>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateServerForumChannel(result.getJsonBody()));
    }


}
