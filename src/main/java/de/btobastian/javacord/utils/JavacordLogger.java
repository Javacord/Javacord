/*
 * Copyright (C) 2017 Bastian Oppermann
 * 
 * This file is part of Javacord.
 * 
 * Javacord is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser general Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Javacord is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.btobastian.javacord.utils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This logger is used if no SLF4J compatible logger was found.
 * It uses the logger classes provided by java itself.
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
    final public boolean isTraceEnabled() {
        // always false
        return false;
    }

    @Override
    final public void trace(String msg) {
        // not used
    }

    @Override
    final public void trace(String format, Object arg) {
        // not used
    }

    @Override
    public final void trace(String format, Object arg1, Object arg2) {
        // not used
    }

    @Override
    public final void trace(String format, Object... arguments) {
        // not used
    }

    @Override
    final public void trace(String msg, Throwable t) {
        // not used
    }

    @Override
    final public boolean isDebugEnabled() {
        return LoggerUtil.isDebug();
    }

    @Override
    final public void debug(String msg) {
        if (isDebugEnabled()) {
            LogRecord record = new LogRecord(Level.FINE, msg);
            record.setLoggerName(name);
            Logger.getLogger(name).log(record);
        }
    }

    @Override
    final public void debug(String format, Object arg1) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg1);
            LogRecord record = new LogRecord(Level.FINE, ft.getMessage());
            record.setThrown(ft.getThrowable());
            record.setLoggerName(name);
            Logger.getLogger(name).log(record);
        }
    }

    @Override
    final public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
            LogRecord record = new LogRecord(Level.FINE, ft.getMessage());
            record.setThrown(ft.getThrowable());
            record.setLoggerName(name);
            Logger.getLogger(name).log(record);
        }
    }

    @Override
    public final void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            LogRecord record = new LogRecord(Level.FINE, ft.getMessage());
            record.setThrown(ft.getThrowable());
            record.setLoggerName(name);
            Logger.getLogger(name).log(record);
        }
    }

    @Override
    final public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            LogRecord record = new LogRecord(Level.FINE, msg);
            record.setThrown(t);
            record.setLoggerName(name);
            Logger.getLogger(name).log(record);
        }
    }

    @Override
    final public boolean isInfoEnabled() {
        // always true
        return true;
    }

    @Override
    final public void info(String msg) {
        LogRecord record = new LogRecord(Level.INFO, msg);
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void info(String format, Object arg1) {
        FormattingTuple ft = MessageFormatter.format(format, arg1);
        LogRecord record = new LogRecord(Level.INFO, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void info(String format, Object arg1, Object arg2) {
        FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
        LogRecord record = new LogRecord(Level.INFO, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    public final void info(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogRecord record = new LogRecord(Level.INFO, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void info(String msg, Throwable t) {
        LogRecord record = new LogRecord(Level.INFO, msg);
        record.setThrown(t);
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public boolean isWarnEnabled() {
        // always true
        return true;
    }

    @Override
    final public void warn(String msg) {
        LogRecord record = new LogRecord(Level.WARNING, msg);
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void warn(String format, Object arg1) {
        FormattingTuple ft = MessageFormatter.format(format, arg1);
        LogRecord record = new LogRecord(Level.WARNING, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void warn(String format, Object arg1, Object arg2) {
        FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
        LogRecord record = new LogRecord(Level.WARNING, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    public final void warn(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogRecord record = new LogRecord(Level.WARNING, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void warn(String msg, Throwable t) {
        LogRecord record = new LogRecord(Level.WARNING, msg);
        record.setThrown(t);
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public boolean isErrorEnabled() {
        // always true
        return true;
    }

    @Override
    final public void error(String msg) {
        LogRecord record = new LogRecord(Level.SEVERE, msg);
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void error(String format, Object arg1) {
        FormattingTuple ft = MessageFormatter.format(format, arg1);
        LogRecord record = new LogRecord(Level.SEVERE, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void error(String format, Object arg1, Object arg2) {
        FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
        LogRecord record = new LogRecord(Level.SEVERE, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    public final void error(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        LogRecord record = new LogRecord(Level.SEVERE, ft.getMessage());
        record.setThrown(ft.getThrowable());
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

    @Override
    final public void error(String msg, Throwable t) {
        LogRecord record = new LogRecord(Level.SEVERE, msg);
        record.setThrown(t);
        record.setLoggerName(name);
        Logger.getLogger(name).log(record);
    }

}
