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
package de.btobastian.javacord;

/**
 * This enum contains all different account types.
 */
public enum AccountType {

    /**
     * A client account.
     * Please notice, that public client bots are not allowed by Discord!
     */
    CLIENT(""),

    /**
     * A bot account.
     */
    BOT("Bot ");

    private final String tokenPrefix;

    /**
     * Class constructor.
     *
     * @param tokenPrefix The prefix, which is added in front of the normal token.
     */
    AccountType(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * Gets the prefix, which is added in front of the normal token.
     *
     * @return The prefix, which is added in front of the normal token.
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

}
