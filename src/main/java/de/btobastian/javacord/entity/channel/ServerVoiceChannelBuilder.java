package de.btobastian.javacord.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server voice channels.
 */
public class ServerVoiceChannelBuilder {

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
    public ServerVoiceChannelBuilder(Server server) {
        this.server = (ImplServer) server;
    }

    /**
     * Sets the reason for this creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelBuilder setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelBuilder setCategory(ChannelCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Sets the bitrate of the channel.
     *
     * @param bitrate The bitrate of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelBuilder setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    /**
     * Sets the user limit of the channel.
     *
     * @param userlimit The user limit of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelBuilder setUserlimit(int userlimit) {
        this.userlimit = userlimit;
        return this;
    }

    /**
     * Creates the server voice channel.
     *
     * @return The created voice channel.
     */
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
