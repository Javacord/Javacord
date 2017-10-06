package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

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
     * If channel is NSFW
     */
    private boolean nsfw = false;
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
     * Sets if channel is NSFW
     * @param nsfw - Value of NSFW
     * @return Current instance to allow chaining.
     */
    public ServerTextChannelBuilder setNSFW(boolean nsfw) {
    	this.nsfw = nsfw;
    	return this;
    }
    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    public CompletableFuture<ServerTextChannel> create() {
        JSONObject body = new JSONObject();
        body.put("type", 0);
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        body.put("name", name);
        if (category != null) {
            body.put("parent_id", String.valueOf(category.getId()));
        }
        body.put("nsfw", nsfw);
        return new RestRequest<ServerTextChannel>(server.getApi(), HttpMethod.POST, RestEndpoint.SERVER_CHANNEL)
                .setUrlParameters(String.valueOf(server.getId()))
                .setBody(body)
                .execute(res -> server.getOrCreateServerTextChannel(res.getBody().getObject()));
    }

}
