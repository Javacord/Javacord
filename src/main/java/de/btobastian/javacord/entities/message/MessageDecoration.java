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
package de.btobastian.javacord.entities.message;

/**
 * All basic decorations available in discord.
 */
public enum MessageDecoration {

    ITALICS("*"),
    BOLD("**"),
    STRIKEOUT("~~"),
    CODE_SIMPLE("`"),
    CODE_LONG("```"),
    UNDERLINE("__");

    private final String prefix;
    private final String suffix;

    private MessageDecoration(String prefix) {
        this.prefix = prefix;
        this.suffix = new StringBuilder(prefix).reverse().toString();
    }

    /**
     * Gets the prefix of the decoration.
     *
     * @return The prefix of the decoration.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the suffix of the decoration.
     *
     * @return The suffix of the decoration.
     */
    public String getSuffix() {
        return suffix;
    }

}
