package org.javacord.core.util.logging;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This logger is used to wrap another logger and replace configured sensitive data by asterisks.
 */
public class PrivacyProtectionLogger implements Logger {

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
     *
     * @param privateData The private data.
     */
    public static void addPrivateData(String privateData) {
        privateDataSet.add(privateData);
    }

    @SuppressWarnings("unchecked")
    private static <T> T filterPrivateData(T target) {
        String targetString = target.toString();
        if (privateDataSet.stream().anyMatch(targetString::contains)) {
            return (T) privateDataSet.stream().reduce(
                    targetString, (s, privateData) -> s.replace(privateData, PRIVATE_DATA_REPLACEMENT));
        } else {
            return target;
        }
    }

    private static Object[] filterPrivateData(Object... targets) {
        return Arrays.stream(targets).map(PrivacyProtectionLogger::filterPrivateData).toArray();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return delegate.isTraceEnabled(marker);
    }

    @Override
    public void trace(String msg) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(filterPrivateData(msg));
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(filterPrivateData(format), filterPrivateData(arguments));
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(filterPrivateData(msg), t);
        }
    }

    @Override
    public void trace(Marker marker, String msg) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(marker, filterPrivateData(msg));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(marker, filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(marker, filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(marker, filterPrivateData(format), filterPrivateData(argArray));
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(marker, filterPrivateData(msg), t);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return delegate.isDebugEnabled(marker);
    }

    @Override
    public void debug(String msg) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(filterPrivateData(msg));
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(filterPrivateData(format), filterPrivateData(arguments));
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(filterPrivateData(msg), t);
        }
    }

    @Override
    public void debug(Marker marker, String msg) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(marker, filterPrivateData(msg));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(marker, filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(marker, filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... argArray) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(marker, filterPrivateData(format), filterPrivateData(argArray));
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(marker, filterPrivateData(msg), t);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return delegate.isInfoEnabled(marker);
    }

    @Override
    public void info(String msg) {
        if (delegate.isInfoEnabled()) {
            delegate.info(filterPrivateData(msg));
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (delegate.isInfoEnabled()) {
            delegate.info(filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (delegate.isInfoEnabled()) {
            delegate.info(filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (delegate.isInfoEnabled()) {
            delegate.info(filterPrivateData(format), filterPrivateData(arguments));
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (delegate.isInfoEnabled()) {
            delegate.info(filterPrivateData(msg), t);
        }
    }

    @Override
    public void info(Marker marker, String msg) {
        if (delegate.isInfoEnabled()) {
            delegate.info(marker, filterPrivateData(msg));
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if (delegate.isInfoEnabled()) {
            delegate.info(marker, filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if (delegate.isInfoEnabled()) {
            delegate.info(marker, filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void info(Marker marker, String format, Object... argArray) {
        if (delegate.isInfoEnabled()) {
            delegate.info(marker, filterPrivateData(format), filterPrivateData(argArray));
        }
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if (delegate.isInfoEnabled()) {
            delegate.info(marker, filterPrivateData(msg), t);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return delegate.isWarnEnabled(marker);
    }

    @Override
    public void warn(String msg) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(filterPrivateData(msg));
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(filterPrivateData(format), filterPrivateData(arguments));
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(filterPrivateData(msg), t);
        }
    }

    @Override
    public void warn(Marker marker, String msg) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(marker, filterPrivateData(msg));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(marker, filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(marker, filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void warn(Marker marker, String format, Object... argArray) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(marker, filterPrivateData(format), filterPrivateData(argArray));
        }
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(marker, filterPrivateData(msg), t);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return delegate.isErrorEnabled(marker);
    }

    @Override
    public void error(String msg) {
        if (delegate.isErrorEnabled()) {
            delegate.error(filterPrivateData(msg));
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (delegate.isErrorEnabled()) {
            delegate.error(filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (delegate.isErrorEnabled()) {
            delegate.error(filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (delegate.isErrorEnabled()) {
            delegate.error(filterPrivateData(format), filterPrivateData(arguments));
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (delegate.isErrorEnabled()) {
            delegate.error(filterPrivateData(msg), t);
        }
    }

    @Override
    public void error(Marker marker, String msg) {
        if (delegate.isErrorEnabled()) {
            delegate.error(marker, filterPrivateData(msg));
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if (delegate.isErrorEnabled()) {
            delegate.error(marker, filterPrivateData(format), filterPrivateData(arg));
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if (delegate.isErrorEnabled()) {
            delegate.error(marker, filterPrivateData(format), filterPrivateData(arg1), filterPrivateData(arg2));
        }
    }

    @Override
    public void error(Marker marker, String format, Object... argArray) {
        if (delegate.isErrorEnabled()) {
            delegate.error(marker, filterPrivateData(format), filterPrivateData(argArray));
        }
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if (delegate.isErrorEnabled()) {
            delegate.error(marker, filterPrivateData(msg), t);
        }
    }

}
