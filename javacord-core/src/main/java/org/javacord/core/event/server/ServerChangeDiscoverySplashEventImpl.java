package org.javacord.core.event.server;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.Icon;
import org.javacord.api.event.server.ServerChangeDiscoverySplashEvent;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link ServerChangeDiscoverySplashEvent}.
 */
public class ServerChangeDiscoverySplashEventImpl extends ServerEventImpl implements ServerChangeDiscoverySplashEvent {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ServerChangeDiscoverySplashEvent.class);

    /**
     * The old discovery splash hash of the server.
     */
    private final String oldDiscoverySplashHash;

    /**
     * The new discovery splash hash of the server.
     */
    private final String newDiscoverySplashHash;

    /**
     * Creates a new discovery splash hash change event.
     *
     * @param server                 The server of the event.
     * @param newDiscoverySplashHash The new discovery splash hash of the server.
     * @param oldDiscoverySplashHash The old discovery splash hash of the server.
     */
    public ServerChangeDiscoverySplashEventImpl(ServerImpl server,
                                                String newDiscoverySplashHash, String oldDiscoverySplashHash) {
        super(server);
        this.oldDiscoverySplashHash = oldDiscoverySplashHash;
        this.newDiscoverySplashHash = newDiscoverySplashHash;
    }

    @Override
    public Optional<Icon> getOldDiscoverySplash() {
        return getDiscoverySplash(oldDiscoverySplashHash);
    }

    @Override
    public Optional<Icon> getNewDiscoverySplash() {
        return getDiscoverySplash(newDiscoverySplashHash);
    }

    /**
     * Gets the discovery splash for the given hash.
     *
     * @param discoverySplashHash The hash of the discovery splash.
     * @return The discovery splash with the given hash.
     */
    private Optional<Icon> getDiscoverySplash(String discoverySplashHash) {
        if (discoverySplashHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new IconImpl(getApi(),
                    new URL("https://cdn.discordapp.com/discovery-splashes/" + getServer().getIdAsString()
                            + "/" + discoverySplashHash + ".png")));
        } catch (MalformedURLException e) {
            throw new AssertionError("Failed to create discovery splash url", e);
        }
    }
}
