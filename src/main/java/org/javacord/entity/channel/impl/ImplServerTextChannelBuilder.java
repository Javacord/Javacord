package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelBuilder;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerTextChannelBuilder}.
 */
public class ImplServerTextChannelBuilder implements ServerTextChannelBuilder {

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
     * The category of the channel.
     */
    private ChannelCategory category = null;

    /**
     * Creates a new server text channel builder.
     *
     * @param server The server of the channel.
     */
    public ImplServerTextChannelBuilder(ImplServer server) {
        this.server = server;
    }

    @Override
    public ServerTextChannelBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ServerTextChannelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServerTextChannelBuilder setCategory(ChannelCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public CompletableFuture<ServerTextChannel> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("type", 0);
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        body.put("name", name);
        if (category != null) {
            body.put("parent_id", category.getIdAsString());
        }
        return new RestRequest<ServerTextChannel>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateServerTextChannel(result.getJsonBody()));
    }


}
