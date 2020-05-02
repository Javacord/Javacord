package org.javacord.core.event.server;

import org.javacord.api.entity.server.ServerFeature;
import org.javacord.api.event.server.ServerChangeServerFeaturesEvent;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * The implementation of {@link ServerChangeServerFeaturesEvent}.
 */
public class ServerChangeServerFeaturesEventImpl extends ServerEventImpl implements ServerChangeServerFeaturesEvent {

    /**
     * The new server feature of the server.
     */
    private final Collection<ServerFeature> newServerFeature;
    /**
     * The old server feature of the server.
     */
    private final Collection<ServerFeature> oldServerFeature;

    /**
     * Creates a new server feature change event.
     *
     * @param server           The server of the event.
     * @param newServerFeature The new server feature of the server.
     * @param oldServerFeature The old server feature of the server.
     */
    public ServerChangeServerFeaturesEventImpl(ServerImpl server,
                                               Collection<ServerFeature> newServerFeature,
                                               Collection<ServerFeature> oldServerFeature) {
        super(server);
        this.oldServerFeature = oldServerFeature;
        this.newServerFeature = newServerFeature;
    }

    @Override
    public Collection<ServerFeature> getOldServerFeatures() {
        return Collections.unmodifiableCollection(new HashSet<>(oldServerFeature));
    }

    @Override
    public Collection<ServerFeature> getNewServerFeatures() {
        return Collections.unmodifiableCollection(new HashSet<>(newServerFeature));
    }
}
