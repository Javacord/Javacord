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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Gets or created a logger with the given name.
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
     * This has only an effect if the default {@link de.btobastian.javacord.utils.JavacordLogger} is used.
     *
     * @param debug Whether debugging should be enabled or not.
     */
    public static void setDebug(boolean debug) {
        LoggerUtil.debug = debug;
    }

    /**
     * Checks whether debugging is enabled or not.
     * This has only an effect if the default {@link de.btobastian.javacord.utils.JavacordLogger} is used.
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
