package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.PositionableServerChannel;
import org.javacord.api.entity.channel.internal.PositionableServerChannelUpdaterDelegate;
import org.javacord.api.entity.channel.internal.ServerChannelUpdaterDelegate;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerChannelUpdaterDelegate}.
 */
public class PositionableServerChannelUpdaterDelegateImpl extends ServerChannelUpdaterDelegateImpl
        implements PositionableServerChannelUpdaterDelegate {

    /**
     * The channel to update.
     */
    protected final PositionableServerChannel channel;

    /**
     * The position to update.
     */
    protected Integer position = null;

    /**
     * Creates a new server channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public PositionableServerChannelUpdaterDelegateImpl(PositionableServerChannel channel) {
        super(channel);
        this.channel = channel;
    }


    @Override
    public void setRawPosition(int rawPosition) {
        this.position = rawPosition;
    }

    @Override
    public CompletableFuture<Void> update() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (prepareUpdateBody(body)) {
            return new RestRequest<Void>(channel.getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                    .setUrlParameters(channel.getIdAsString())
                    .setBody(body)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = false;

        if (position != null) {
            body.put("position", position.intValue());
            patchChannel = true;
        }

        return patchChannel;
    }

}
