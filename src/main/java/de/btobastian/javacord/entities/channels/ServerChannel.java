package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.listeners.server.channel.ServerChannelChangeNameListener;
import de.btobastian.javacord.listeners.server.channel.ServerChannelDeleteListener;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server channel.
 */
public interface ServerChannel extends Channel {

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    String getName();

    /**
     * Gets the server of the channel.
     *
     * @return The server of the channel.
     */
    Server getServer();

    /**
     * Gets the position of the channel.
     *
     * @return The position of the channel.
     */
    int getPosition();

    /**
     * Deletes the channel.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), HttpMethod.DELETE, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .execute(res -> null);
    }

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     */
    void addServerChannelDeleteListener(ServerChannelDeleteListener listener);

    /**
     * Gets a list with all registered server channel delete listeners.
     *
     * @return A list with all registered server channel delete listeners.
     */
    List<ServerChannelDeleteListener> getServerChannelDeleteListeners();

    /**
     * Adds a listener, which listens to server channel name changes.
     *
     * @param listener The listener to add.
     */
    void addServerChannelChangeNameListener(ServerChannelChangeNameListener listener);

    /**
     * Gets a list with all registered server channel change name listeners.
     *
     * @return A list with all registered server channel change name listeners.
     */
    List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners();

}
