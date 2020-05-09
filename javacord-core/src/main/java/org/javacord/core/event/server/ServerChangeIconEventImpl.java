package org.javacord.core.event.server;

import org.apache.logging.log4j.Logger;
import org.javacord.api.Javacord;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeIconEvent;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link ServerChangeIconEvent}.
 */
public class ServerChangeIconEventImpl extends ServerEventImpl implements ServerChangeIconEvent {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ServerChangeIconEvent.class);

    /**
     * The new icon hash of the server.
     */
    private final String newIconHash;

    /**
     * The old icon hash of the server.
     */
    private final String oldIconHash;

    /**
     * Creates a new server change icon event.
     *
     * @param server The server of the event.
     * @param newIconHash The new icon hash of the server.
     * @param oldIconHash The old icon hash of the server.
     */
    public ServerChangeIconEventImpl(Server server, String newIconHash, String oldIconHash) {
        super(server);
        this.newIconHash = newIconHash;
        this.oldIconHash = oldIconHash;
    }

    @Override
    public Optional<Icon> getOldIcon() {
        return getIcon(oldIconHash);
    }

    @Override
    public Optional<Icon> getNewIcon() {
        return getIcon(newIconHash);
    }

    /**
     * Gets the icon for the given hash.
     *
     * @param iconHash The hash of the icon.
     * @return The icon with the given hash.
     */
    private Optional<Icon> getIcon(String iconHash) {
        if (iconHash == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new IconImpl(getApi(), new URL(
                    "https://" + Javacord.DISCORD_CDN_DOMAIN + "/icons/" + getServer().getIdAsString()
                            + "/" + iconHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

}
