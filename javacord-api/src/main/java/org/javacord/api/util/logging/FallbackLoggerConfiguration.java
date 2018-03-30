package org.javacord.api.util.logging;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class can be used to configure Javacord's fallback logger.
 */
public class FallbackLoggerConfiguration {

    private static final AtomicBoolean debug = new AtomicBoolean();

    private static final AtomicBoolean trace = new AtomicBoolean();

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
     * Disabling debug logging automatically disables trace logging, too.
     *
     * @param debug Whether debug logging should be enabled or not.
     */
    public static void setDebug(boolean debug) {
        FallbackLoggerConfiguration.debug.set(debug);
        if (!debug) {
            trace.set(false);
        }
    }

    /**
     * Checks whether trace logging is enabled.
     *
     * @return Whether trace logging is enabled or not.
     */
    public static boolean isTraceEnabled() {
        return trace.get();
    }

    /**
     * Sets whether trace logging should be enabled.
     * Enabling trace logging automatically enables debug logging, too.
     *
     * @param trace Whether trace logging should be enabled or not.
     */
    public static void setTrace(boolean trace) {
        FallbackLoggerConfiguration.trace.set(trace);
        if (trace) {
            debug.set(true);
        }
    }

}
