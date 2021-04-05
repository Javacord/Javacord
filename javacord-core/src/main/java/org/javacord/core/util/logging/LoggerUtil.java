package org.javacord.core.util.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.util.Map;
import java.util.Properties;
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
            if (!initialized && !ProviderUtil.hasProviders()) {
                noLogger.set(true);
                logWarning.set(true);
            }
            return true;
        });

        if (noLogger.get()) {
            return loggers.computeIfAbsent(name, key -> {
                Level level = FallbackLoggerConfiguration.isTraceEnabled()
                        ? Level.TRACE
                        : (FallbackLoggerConfiguration.isDebugEnabled() ? Level.DEBUG : Level.INFO);
                Logger logger = new SimpleLogger(name, level, true, false, true, true, "yyyy-MM-dd HH:mm:ss.SSSZ", null,
                                                 new PropertiesUtil(new Properties()), System.out);
                if (logWarning.get()) {
                    logger.info("No Log4j2 compatible logger was found. Using default Javacord implementation!");
                }
                return new PrivacyProtectionLogger(logger);
            });
        } else {
            return new PrivacyProtectionLogger(LogManager.getLogger(name));
        }
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
     * Logs if a channel is missing for a specific event.
     *
     * @param logger The logger of the event.
     * @param channelId The id of the missing channel.
     */
    public static void logMissingChannel(Logger logger, long channelId) {
        logger.warn("Couldn't get the Channel with the id {} for a {}. Please update to the latest "
                + "Javacord version or create an issue on the Javacord GitHub page if you are already "
                + "on the latest version.", channelId, logger.getName());
    }

}
