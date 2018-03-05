package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerVoiceChannelBuilder}.
 */
public class ImplServerVoiceChannelBuilder implements ServerVoiceChannelBuilder {

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
     * The bitrate of the channel.
     */
    private Integer bitrate = null;

    /**
     * The userlimit of the channel.
     */
    private Integer userlimit = null;

    /**
     * The category of the channel.
     */
    private ChannelCategory category = null;

    /**
     * Creates a new server voice channel builder.
     *
     * @param server The server of the channel.
     */
    public ImplServerVoiceChannelBuilder(ImplServer server) {
        this.server = server;
    }

    @Override
    public ServerVoiceChannelBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public ServerVoiceChannelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ServerVoiceChannelBuilder setCategory(ChannelCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public ServerVoiceChannelBuilder setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    @Override
    public ServerVoiceChannelBuilder setUserlimit(int userlimit) {
        this.userlimit = userlimit;
        return this;
    }

    @Override
    public CompletableFuture<ServerVoiceChannel> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("type", 2);
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        body.put("name", name);
        if (category != null) {
            body.put("parent_id", category.getIdAsString());
        }
        if (bitrate != null) {
            body.put("bitrate", (int) bitrate);
        }
        if (userlimit != null) {
            body.put("user_limit", (int) userlimit);
        }
        return new RestRequest<ServerVoiceChannel>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(server.getIdAsString())
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> server.getOrCreateServerVoiceChannel(result.getJsonBody()));
    }


}
