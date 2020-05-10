package org.javacord.api.event.server;

import java.util.Locale;

/**
 * A server change preferred locale event.
 */
public interface ServerChangePreferredLocaleEvent extends ServerEvent {

    /**
     * Gets the old preferred locale of the server.
     *
     * @return The old preferred locale of the server.
     */
    Locale getOldPreferredLocale();

    /**
     * Gets the new preferred locale of the server.
     *
     * @return The new preferred locale of the server.
     */
    Locale getNewPreferredLocale();

}
