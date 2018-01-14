package de.btobastian.javacord.utils.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;

import java.util.HashMap;

/**
 * This class is used to get a {@link Logger} instance.
 */
public class LoggerUtil {

    private static volatile boolean initialized = false;
    // we cannot use a boolean as lock so we need an extra lock object
    private static final Object initLock = new Object();

    private static final HashMap<String, Logger> loggers = new HashMap<>();
    private static volatile boolean noLogger = false;
    private static volatile boolean debug = false;

    /**
     * Get or create a logger with the given name.
     *
     * @param name The name of the logger.
     * @return The logger with the given name.
     */
    public static Logger getLogger(String name) {
        synchronized (initLock) {
            if (!initialized) {
                init();
            }
        }
        if (noLogger) { // we don't want the SLF4J NOPLogger implementation
            synchronized (loggers) {
                Logger logger = loggers.get(name);
                if (logger == null) {
                    logger = new JavacordLogger(name);
                    loggers.put(name, logger);
                }
                return logger;
            }
        } else {
            return LoggerFactory.getLogger(name);
        }
    }

    /**
     * Put a diagnostic context value (the {@code val} parameter) as identified with the
     * {@code key} parameter into the current thread's diagnostic context map. The
     * {@code key} parameter cannot be {@code null}. The {@code val} parameter
     * can be {@code null} only if the underlying implementation supports it.
     *
     * <p>
     * This method delegates all work to the MDC of the underlying logging system
     * and is a no-op if there is no proper logging binding present, also regarding {@code null} key.
     * <p>
     * This method returns a {@code Closeable} object which can remove {@code key} when
     * {@code close} is called.
     *
     * <p>
     * Useful with Java 7 for example :
     * {@code
     *     try (MDCCloseable closeable = LoggerUtil.putCloseableToMdc(key, value)) {
     *         ....
     *     }
     * }
     *
     * @param key non-{@code null} key
     * @param val value to put in the map
     * @return a {@code Closeable} which can remove {@code key} when {@code close} is called.
     * @throws IllegalArgumentException in case the {@code key} parameter is {@code null} and the method is not a no-op
     */
    public static MDCCloseable putCloseableToMdc(String key, String val) throws IllegalArgumentException {
        return noLogger ? null : MDC.putCloseable(key, val);
    }

    /**
     * Gets or creates a logger for the given name.
     *
     * @param clazz The class of the logger.
     * @return A logger for the given class.
     */
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Sets whether debugging should be enabled or not.
     * This has only an effect if the default {@link JavacordLogger} is used.
     *
     * @param debug Whether debugging should be enabled or not.
     */
    public static void setDebug(boolean debug) {
        LoggerUtil.debug = debug;
    }

    /**
     * Checks whether debugging is enabled or not.
     * This has only an effect if the default {@link JavacordLogger} is used.
     *
     * @return Whether debugging is enabled or not.
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Initializes the logger util.
     */
    private static void init() {
        initialized = true;
        try {
            // if there's no library this would cause a ClassNotFoundException
            Class.forName("org.slf4j.impl.StaticLoggerBinder");
        } catch (ClassNotFoundException e) {
            noLogger = true;
            getLogger(LoggerUtil.class)
                    .info("No SLF4J compatible logger was found. Using default javacord implementation!");
        }
    }

}