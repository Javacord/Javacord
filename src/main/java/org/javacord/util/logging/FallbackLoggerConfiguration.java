package org.javacord.util.logging;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class can be used to configure Javacord's fallback logger.
 */
public class FallbackLoggerConfiguration {

    private static AtomicBoolean debug = new AtomicBoolean();

    private FallbackLoggerConfiguration() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks whether debug logging is enabled.
     *
     * @return Whether debug logging is enabled or not.
     */
    public static boolean isDebugEnabled() {
        return debug.get();
    }

    /**
     * Sets whether debug logging should be enabled.
     *
     * @param debug Whether debug logging should be enabled or not.
     */
    public static void setDebug(boolean debug) {
        FallbackLoggerConfiguration.debug.set(debug);
    }

}
