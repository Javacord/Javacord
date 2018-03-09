package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.ServerVoiceChannelBuilderDelegate;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerVoiceChannelBuilderDelegate}.
 */
public class ImplServerVoiceChannelBuilderDelegate implements ServerVoiceChannelBuilderDelegate {

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
     * Creates a new server voice channel builder delegate.
     *
     * @param server The server of the server voice channel.
     */
    public ImplServerVoiceChannelBuilderDelegate(ImplServer server) {
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
    public void setCategory(ChannelCategory category) {
        this.category = category;
    }

    @Override
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    @Override
    public void setUserlimit(int userlimit) {
        this.userlimit = userlimit;
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
