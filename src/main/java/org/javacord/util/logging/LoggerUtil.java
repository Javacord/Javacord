package org.javacord.util.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is used to get a {@link Logger} instance.
 */
public class LoggerUtil {

    private static final AtomicReference<Boolean> initialized = new AtomicReference<>(false);
    private static final AtomicBoolean noLogger = new AtomicBoolean();
    private static final Map<String, Logger> loggers = new ConcurrentHashMap<>();

    /**
     * Get or create a logger with the given name.
     *
     * @param name The name of the logger.
     * @return The logger with the given name.
     */
    public static Logger getLogger(String name) {
        AtomicBoolean logWarning = new AtomicBoolean(false);
        initialized.updateAndGet(initialized -> {
            if (!initialized) {
                try {
                    // if there's no library this would cause a ClassNotFoundException
                    Class.forName("org.slf4j.impl.StaticLoggerBinder");
                } catch (ClassNotFoundException e) {
                    noLogger.set(true);
                    logWarning.set(true);
                }
            }
            return true;
        });

        if (noLogger.get()) { // we don't want the SLF4J NOPLogger implementation
            return loggers.computeIfAbsent(name, key -> {
                JavacordLogger logger = new JavacordLogger(name);
                if (logWarning.get()) {
                    logger.info("No SLF4J compatible logger was found. Using default javacord implementation!");
                }
                return logger;
            });
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
        return noLogger.get() ? null : MDC.putCloseable(key, val);
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

}
