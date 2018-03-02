package de.btobastian.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import de.btobastian.javacord.entity.channel.ChannelCategory;
import de.btobastian.javacord.entity.channel.ChannelCategoryBuilder;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ChannelCategoryBuilder}.
 */
public class ImplChannelCategoryBuilder implements ChannelCategoryBuilder {

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
     * Creates a new channel category builder.
     *
     * @param server The server of the channel.
     */
    public ImplChannelCategoryBuilder(ImplServer server) {
        this.server = server;
    }

    @Override
    public ChannelCategoryBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ChannelCategoryBuilder setName(String name) {
        this.name = name;
        return this;
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
