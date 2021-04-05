package org.javacord.core.util.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.spi.AbstractLogger;

import java.util.HashSet;
import java.util.Set;

/**
 * This logger is used to wrap another logger and replace configured sensitive data by asterisks.
 */
public class PrivacyProtectionLogger extends AbstractLogger {

    private static final String PRIVATE_DATA_REPLACEMENT = "**********";
    private static final Set<String> privateDataSet = new HashSet<>();

    private final Logger delegate;

    /**
     * Class constructor.
     * It's recommended to use {@link LoggerUtil#getLogger(String)}.
     *
     * @param delegate The delegate logger that gets the cleaned messages.
     */
    PrivacyProtectionLogger(Logger delegate) {
        this.delegate = delegate;
    }

    /**
     * Adds a private data to be asterisked out in log messages.
     * A {@code null} argument is simply ignored.
     *
     * @param privateData The private data.
     */
    public static void addPrivateData(String privateData) {
        if (privateData != null && !privateData.trim().isEmpty()) {
            privateDataSet.add(privateData);
        }
    }

    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        String formattedMessage = message.getFormattedMessage();
        if (privateDataSet.stream().anyMatch(formattedMessage::contains)) {
            delegate.log(level, marker, privateDataSet.stream().reduce(
                    formattedMessage, (s, privateData) -> s.replace(privateData, PRIVATE_DATA_REPLACEMENT)), t);
        } else {
            delegate.log(level, marker, message, t);
        }
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
                             Object p4) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
                             Object p4, Object p5) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
                             Object p4, Object p5, Object p6) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
                             Object p4, Object p5, Object p6, Object p7) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
                             Object p4, Object p5, Object p6, Object p7, Object p8) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3,
                             Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return delegate.isEnabled(level, marker);
    }

    @Override
    public Level getLevel() {
        return delegate.getLevel();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
