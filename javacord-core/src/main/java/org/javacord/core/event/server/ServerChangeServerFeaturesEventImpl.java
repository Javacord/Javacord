package org.javacord.core.event.server;

import org.javacord.api.entity.server.ServerFeature;
import org.javacord.api.event.server.ServerChangeServerFeaturesEvent;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Collections;
import java.util.Set;

/**
 * The implementation of {@link ServerChangeServerFeaturesEvent}.
 */
public class ServerChangeServerFeaturesEventImpl extends ServerEventImpl implements ServerChangeServerFeaturesEvent {

    /**
     * The new server feature of the server.
     */
    private final Set<ServerFeature> newServerFeature;
    /**
     * The old server feature of the server.
     */
    private final Set<ServerFeature> oldServerFeature;

    /**
     * Creates a new server feature change event.
     *
     * @param server           The server of the event.
     * @param newServerFeature The new server feature of the server.
     * @param oldServerFeature The old server feature of the server.
     */
    public ServerChangeServerFeaturesEventImpl(ServerImpl server,
                                               Set<ServerFeature> newServerFeature,
                                               Set<ServerFeature> oldServerFeature) {
        super(server);
        this.oldServerFeature = oldServerFeature;
        this.newServerFeature = newServerFeature;
    }

    @Override
    public Set<ServerFeature> getOldServerFeatures() {
        return Collections.unmodifiableSet(oldServerFeature);
    }

    @Override
    public Set<ServerFeature> getNewServerFeatures() {
        return Collections.unmodifiableSet(newServerFeature);
    }
}
