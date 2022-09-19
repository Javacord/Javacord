package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.internal.ChannelCategoryBuilderDelegate;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ChannelCategoryBuilderDelegate}.
 */
public class ChannelCategoryBuilderDelegateImpl extends RegularServerChannelBuilderDelegateImpl
        implements ChannelCategoryBuilderDelegate {

    /**
     * Creates a new channel category builder delegate.
     *
     * @param server The server of the channel category.
     */
    public ChannelCategoryBuilderDelegateImpl(ServerImpl server) {
        super(server);
    }

    @Override
    public CompletableFuture<ChannelCategory> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode()
                .put("type", 4);
        super.prepareBody(body);
        return new RestRequest<ChannelCategory>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateChannelCategory(result.getJsonBody()));
    }

}
