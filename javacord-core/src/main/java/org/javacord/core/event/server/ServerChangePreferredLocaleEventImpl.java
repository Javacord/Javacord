package org.javacord.core.event.server;

import org.javacord.api.event.server.ServerChangePreferredLocaleEvent;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Locale;

/**
 * The implementation of {@link ServerChangePreferredLocaleEvent}.
 */
public class ServerChangePreferredLocaleEventImpl extends ServerEventImpl implements ServerChangePreferredLocaleEvent {

    /**
     * The new preferred locale of the server.
     */
    private final Locale newPreferredLocale;
    /**
     * The old preferred locale of the server.
     */
    private final Locale oldPreferredLocale;

    /**
     * Creates a new preferred locale change event.
     *
     * @param server             The server of the event.
     * @param oldPreferredLocale The new preferred locale of the server.
     * @param newPreferredLocale The old preferred locale of the server.
     */
    public ServerChangePreferredLocaleEventImpl(ServerImpl server,
                                                Locale newPreferredLocale, Locale oldPreferredLocale) {
        super(server);
        this.oldPreferredLocale = oldPreferredLocale;
        this.newPreferredLocale = newPreferredLocale;
    }

    @Override
    public Locale getOldPreferredLocale() {
        return oldPreferredLocale;
    }

    @Override
    public Locale getNewPreferredLocale() {
        return newPreferredLocale;
    }
}
