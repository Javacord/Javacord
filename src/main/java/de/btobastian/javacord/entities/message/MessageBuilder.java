/*
 * Copyright (C) 2016 Bastian Oppermann
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

import de.btobastian.javacord.entities.User;

/**
 * This class helps to build messages with decorations.
 */
public class MessageBuilder {

    private StringBuilder strBuilder;

    /**
     * Creates a new instance of this class.
     */
    public MessageBuilder() {
        strBuilder = new StringBuilder();
    }

    /**
     * Appends the given message to the text (without decorations).
     *
     * @param message The message.
     * @return This object to reuse it.
     */
    public MessageBuilder append(String message) {
        strBuilder.append(message);
        return this;
    }

    /**
     * Appends the decoration to the text.
     *
     * @param decoration The decoration/style.
     * @param message The message.
     * @return This object to reuse it.
     */
    public MessageBuilder appendDecoration(MessageDecoration decoration, String message) {
        strBuilder.append(decoration.getPrefix()).append(message).append(decoration.getSuffix());
        return this;
    }

    /**
     * Appends code to the text.
     *
     * @param language The language, e.g. "java".
     * @param message The message.
     * @return This object to reuse it.
     */
    public MessageBuilder appendCode(String language, String message) {
        strBuilder.append(MessageDecoration.CODE_LONG.getPrefix()).append(language).append("\n")
                .append(message).append(MessageDecoration.CODE_LONG.getSuffix());
        return this;
    }

    /**
     * Appends an user (@user).
     *
     * @param user The user to append.
     * @return This object to reuse it.
     */
    public MessageBuilder appendMention(User user) {
        strBuilder.append("<@").append(user.getId()).append(">");
        return this;
    }

    /**
     * Generates the String to send.
     *
     * @return The String to send.
     */
    public String build() {
        return strBuilder.toString();
    }

}