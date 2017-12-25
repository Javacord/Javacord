package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server channels.
 */
public class ServerChannelUpdater {

    /**
     * The channel to update.
     */
    protected final ServerChannel channel;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * The position to update.
     */
    protected Integer position = null;

    /**
     * Creates a new server channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerChannelUpdater(ServerChannel channel) {
        this.channel = channel;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Queues the position to be updated.
     *
     * @param position The new position of the channel.
     *                 If you want to update the position based on other channels, make sure to use
     *                 {@link ServerChannel#getRawPosition()} instead of {@link ServerChannel#getPosition()}!
     * @return The current instance in order to chain call methods.
     */
    public ServerChannelUpdater setPosition(int position) {
        this.position = position;
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        boolean patchChannel = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchChannel = true;
        }
        if (position != null) {
            body.put("position", position.intValue());
            patchChannel = true;
        }
        if (patchChannel) {
            return new RestRequest<Void>(channel.getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                    .setUrlParameters(channel.getIdAsString())
                    .setBody(body)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}
