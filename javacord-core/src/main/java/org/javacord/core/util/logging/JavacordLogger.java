package org.javacord.core.util.logging;

import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This logger is used if no SLF4J compatible logger was found.
 * It uses the the default output stream from {@link System#out} and {@link System#err}.
 * This is a really primitive implementation and not recommended in production. Use a slf4j compatible logger instead!
 */
public class JavacordLogger extends MarkerIgnoringBase {

    private final String name;

    /**
     * Class constructor.
     * It's recommended to use {@link LoggerUtil#getLogger(String)}.
     *
     * @param name The name of the logger.
     */
    public JavacordLogger(String name) {
        this.name = name;
        // set level to all. We check if debug is enabled ourselves
        Logger.getLogger(name).setLevel(Level.ALL);
    }

    @Override

    public String getName() {
        return name;
    }

    @Override
    public final boolean isTraceEnabled() {
        return FallbackLoggerConfiguration.isTraceEnabled();
    }

    @Override
    public final void trace(String msg) {
        if (isTraceEnabled()) {
            log("TRACE", msg, null);
        }
    }

    @Override
    public final void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            log("TRACE", ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public final void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            log("TRACE", ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public final void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            log("TRACE", ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public final void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            log("TRACE", msg, t);
        }
    }

    @Override
    public final boolean isDebugEnabled() {
        return FallbackLoggerConfiguration.isDebugEnabled();
    }

    @Override
    public final void debug(String msg) {
        if (isDebugEnabled()) {
            log("DEBUG", msg, null);
        }
    }

    @Override
    public final void debug(String format, Object arg1) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg1);
            log("DEBUG", ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public final void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            log("DEBUG", ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public final void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            log("DEBUG", ft.getMessage(), ft.getThrowable());
        }
    }

    @Override
    public final void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            log("DEBUG", msg, t);
        }
    }

    @Override
    public final boolean isInfoEnabled() {
        // always true
        return true;
    }

    @Override
    public final void info(String msg) {
        log("INFO", msg, null);
    }

    @Override
    public final void info(String format, Object arg1) {
        FormattingTuple ft = MessageFormatter.format(format, arg1);
        log("INFO", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void info(String format, Object arg1, Object arg2) {
        FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
        log("INFO", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void info(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log("INFO", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void info(String msg, Throwable t) {
        LogRecord record = new LogRecord(Level.INFO, msg);
        log("INFO", msg, t);
    }

    @Override
    public final boolean isWarnEnabled() {
        // always true
        return true;
    }

    @Override
    public final void warn(String msg) {
        log("WARNING", msg, null);
    }

    @Override
    public final void warn(String format, Object arg1) {
        FormattingTuple ft = MessageFormatter.format(format, arg1);
        log("WARNING", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void warn(String format, Object arg1, Object arg2) {
        FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
        log("WARNING", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void warn(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log("WARNING", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void warn(String msg, Throwable t) {
        log("WARNING", msg, t);
    }

    @Override
    public final boolean isErrorEnabled() {
        // always true
        return true;
    }

    @Override
    public final void error(String msg) {
        log("ERROR", msg, null);
    }

    @Override
    public final void error(String format, Object arg1) {
        FormattingTuple ft = MessageFormatter.format(format, arg1);
        log("ERROR", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void error(String format, Object arg1, Object arg2) {
        FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
        log("ERROR", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void error(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log("ERROR", ft.getMessage(), ft.getThrowable());
    }

    @Override
    public final void error(String msg, Throwable t) {
        log("ERROR", msg, t);
    }

    /**
     * Simply logs the to the default output stream {@link System#out} and errors to {@link System#err}.
     *
     * @param level The level of the log message.
     * @param msg The text of the log message.
     * @param t The exception.
     */
    private void log(String level, String msg, Throwable t) {
        PrintStream printStream = level.equals("ERROR") || t != null ? System.err : System.out;
        if (msg != null && t == null) {
            printStream.println("[" + level + "][" + Thread.currentThread().getName() + "][" + name + "] " + msg);
        }
        if (t != null) {
            if (msg != null) {
                printStream.println("[" + level + "][" + Thread.currentThread().getName() + "][" + name + "] " + msg);
            }
            t.printStackTrace();
        }
    }

}
