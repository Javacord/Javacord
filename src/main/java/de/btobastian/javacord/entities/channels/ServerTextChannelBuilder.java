package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server text channels.
 */
public class ServerTextChannelBuilder {

    /**
     * The server of the channel.
     */
    private ImplServer server;

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
    public ServerTextChannelBuilder(Server server) {
        this.server = (ImplServer) server;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelBuilder setCategory(ChannelCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    public CompletableFuture<ServerTextChannel> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("type", 0);
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        body.put("name", name);
        if (category != null) {
            body.put("parent_id", String.valueOf(category.getId()));
        }
        return new RestRequest<ServerTextChannel>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(String.valueOf(server.getId()))
                .setBody(body)
                .execute(result -> server.getOrCreateServerTextChannel(result.getJsonBody()));
    }

}
