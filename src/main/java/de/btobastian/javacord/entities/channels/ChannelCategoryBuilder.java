package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new channel categories.
 */
public class ChannelCategoryBuilder {

    /**
     * The server of the channel.
     */
    private ImplServer server;

    /**
     * The name of the channel.
     */
    private String name = null;

    /**
     * Creates a new channel category builder.
     *
     * @param server The server of the channel.
     */
    public ChannelCategoryBuilder(Server server) {
        this.server = (ImplServer) server;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ChannelCategoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Creates the channel category.
     *
     * @return The created channel category.
     */
    public CompletableFuture<ChannelCategory> create() {
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        return new RestRequest<ChannelCategory>(server.getApi(), RestMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(String.valueOf(server.getId()))
                .setBody(JsonNodeFactory.instance.objectNode().put("type", 4).put("name", name))
                .execute((res, json) -> server.getOrCreateChannelCategory(json));
    }

}
