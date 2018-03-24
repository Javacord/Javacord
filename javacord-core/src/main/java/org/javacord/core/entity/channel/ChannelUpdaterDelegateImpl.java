package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.channel.internal.GroupChannelUpdaterDelegate;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link GroupChannelUpdaterDelegate}.
 */
public class ChannelUpdaterDelegateImpl implements GroupChannelUpdaterDelegate {

    /**
     * The channel to update.
     */
    protected final GroupChannel channel;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * Creates a new group channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public ChannelUpdaterDelegateImpl(GroupChannel channel) {
        this.channel = channel;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
