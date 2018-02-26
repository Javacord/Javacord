package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * A server change splash event.
 */
public class ServerChangeSplashEvent extends ServerEvent {

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
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newSplashHash The new splash hash of the server.
     * @param oldSplashHash The old splash hash of the server.
     */
    public ServerChangeSplashEvent(DiscordApi api, Server server, String newSplashHash, String oldSplashHash) {
        super(api, server);
        this.newSplashHash = newSplashHash;
        this.oldSplashHash = oldSplashHash;
    }

    /**
     * Gets the old splash of the server.
     *
     * @return The old splash of the server.
     */
    public Optional<Icon> getOldSplash() {
        return getSplash(oldSplashHash);
    }

    /**
     * Gets the new splash of the server.
     *
     * @return The new splash of the server.
     */
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
