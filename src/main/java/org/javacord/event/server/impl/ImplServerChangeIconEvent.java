package org.javacord.event.server.impl;

import org.javacord.entity.Icon;
import org.javacord.entity.impl.ImplIcon;
import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeIconEvent;
import org.javacord.util.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of {@link ServerChangeIconEvent}.
 */
public class ImplServerChangeIconEvent extends ImplServerEvent implements ServerChangeIconEvent {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ServerChangeIconEvent.class);

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
    public ImplServerChangeIconEvent(Server server, String newIconHash, String oldIconHash) {
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
            return Optional.of(new ImplIcon(getApi(),
                                            new URL("https://cdn.discordapp.com/icons/" + getServer().getIdAsString()
                                                    + "/" + iconHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

}
