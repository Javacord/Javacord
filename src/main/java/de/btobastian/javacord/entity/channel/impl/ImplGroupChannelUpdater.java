package de.btobastian.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.channel.GroupChannel;
import de.btobastian.javacord.entity.channel.GroupChannelUpdater;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link GroupChannelUpdater}.
 */
public class ImplGroupChannelUpdater implements GroupChannelUpdater {

    /**
     * The channel to update.
     */
    protected final GroupChannel channel;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * Creates a new group channel updater.
     *
     * @param channel The channel to update.
     */
    public ImplGroupChannelUpdater(GroupChannel channel) {
        this.channel = channel;
    }

    @Override
    public GroupChannelUpdater setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        boolean patchChannel = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
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
