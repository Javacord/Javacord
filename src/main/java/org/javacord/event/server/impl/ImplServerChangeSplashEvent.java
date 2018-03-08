package org.javacord.event.server.impl;

import org.javacord.entity.Icon;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeSplashEvent;
import org.javacord.util.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link ServerChangeSplashEvent}.
 */
public class ImplServerChangeSplashEvent extends ImplServerEvent implements ServerChangeSplashEvent {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ServerChangeSplashEvent.class);

    /**
     * The new splash hash of the server.
     */
    private final String newSplashHash;

    /**
     * The old splash hash of the server.
     */
    private final String oldSplashHash;

    /**
     * Creates a new server change splash event.
     *
     * @param server The server of the event.
     * @param newSplashHash The new splash hash of the server.
     * @param oldSplashHash The old splash hash of the server.
     */
    public ImplServerChangeSplashEvent(Server server, String newSplashHash, String oldSplashHash) {
        super(server);
        this.newSplashHash = newSplashHash;
        this.oldSplashHash = oldSplashHash;
    }

    @Override
    public Optional<Icon> getOldSplash() {
        return getSplash(oldSplashHash);
    }

    @Override
    public Optional<Icon> getNewSplash() {
        return getSplash(newSplashHash);
    }

    /**
     * Gets the splash for the given hash.
     *
     * @param splashHash The hash of the splash.
     * @return The splash with the given hash.
     */
    private Optional<Icon> getSplash(String splashHash) {
        if (splashHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ImplIcon(getApi(),
                                            new URL("https://cdn.discordapp.com/splashs/" + getServer().getIdAsString()
                                                    + "/" + splashHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the splash is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

}
