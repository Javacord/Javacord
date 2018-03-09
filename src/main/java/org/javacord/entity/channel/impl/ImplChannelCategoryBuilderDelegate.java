package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ChannelCategoryBuilderDelegate;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ChannelCategoryBuilderDelegate}.
 */
public class ImplChannelCategoryBuilderDelegate implements ChannelCategoryBuilderDelegate {

    /**
     * The server of the channel.
     */
    private ImplServer server;

    /**
     * The reason for the creation.
     */
    private String reason = null;

    /**
     * The name of the channel.
     */
    private String name = null;

    /**
     * Creates a new channel category builder delegate.
     *
     * @param server The server of the channel category.
     */
    public ImplChannelCategoryBuilderDelegate(ImplServer server) {
        this.server = server;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CompletableFuture<ChannelCategory> create() {
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        return new RestRequest<ChannelCategory>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(server.getIdAsString())
                .setBody(JsonNodeFactory.instance.objectNode().put("type", 4).put("name", name))
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateChannelCategory(result.getJsonBody()));
    }

}
