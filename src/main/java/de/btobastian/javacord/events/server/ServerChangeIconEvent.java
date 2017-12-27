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
 * A server change icon event.
 */
public class ServerChangeIconEvent extends ServerEvent {

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
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newIconHash The new icon hash of the server.
     * @param oldIconHash The old icon hash of the server.
     */
    public ServerChangeIconEvent(DiscordApi api, Server server, String newIconHash, String oldIconHash) {
        super(api, server);
        this.newIconHash = newIconHash;
        this.oldIconHash = oldIconHash;
    }

    /**
     * Gets the old icon of the server.
     *
     * @return The old icon of the server.
     */
    public Optional<Icon> getOldIcon() {
        return getIcon(oldIconHash);
    }

    /**
     * Gets the new icon of the server.
     *
     * @return The new icon of the server.
     */
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
                    new URL("https://cdn.discordapp.com/icons/" + getServer().getId() + "/" + iconHash + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

}
