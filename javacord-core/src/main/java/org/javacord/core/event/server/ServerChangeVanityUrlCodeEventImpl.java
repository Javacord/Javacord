package org.javacord.core.event.server;

import org.javacord.api.entity.VanityUrlCode;
import org.javacord.api.event.server.ServerChangeVanityUrlCodeEvent;
import org.javacord.core.entity.VanityUrlCodeImpl;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Optional;

/**
 * The implementation of {@link ServerChangeVanityUrlCodeEvent}.
 */
public class ServerChangeVanityUrlCodeEventImpl extends ServerEventImpl implements ServerChangeVanityUrlCodeEvent {

    /**
     * The new vanity url code of the server.
     */
    private final String newVanityUrlCode;

    /**
     * The old vanity url code  of the server.
     */
    private final String oldVanityCode;

    /**
     * Creates a new vanity url code change event.
     *
     * @param server        The server of the event.
     * @param newVanityCode The new vanity url code of the server.
     * @param oldVanityCode The old vanity url code of the server.
     */
    public ServerChangeVanityUrlCodeEventImpl(ServerImpl server, String newVanityCode, String oldVanityCode) {
        super(server);
        this.oldVanityCode = oldVanityCode;
        this.newVanityUrlCode = newVanityCode;
    }

    @Override
    public Optional<VanityUrlCode> getOldVanityUrlCode() {
        return Optional.ofNullable(oldVanityCode == null ? null : new VanityUrlCodeImpl(oldVanityCode));
    }

    @Override
    public Optional<VanityUrlCode> getNewVanityUrlCode() {
        return Optional.ofNullable(newVanityUrlCode == null ? null : new VanityUrlCodeImpl(newVanityUrlCode));
    }
}
