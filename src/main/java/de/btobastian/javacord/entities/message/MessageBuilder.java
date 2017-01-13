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

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;

/**
 * This class helps to build messages with decorations.
 */
public class MessageBuilder {

    private final StringBuilder strBuilder;

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
     * @param message The message.
     * @param decorations The decorations/style.
     * @return This object to reuse it.
     */
    public MessageBuilder appendDecoration(String message, MessageDecoration... decorations) {
        for (MessageDecoration decoration : decorations) {
            strBuilder.append(decoration.getPrefix());
        }
        strBuilder.append(message);
        for (MessageDecoration decoration : decorations) {
            strBuilder.append(decoration.getSuffix());
        }
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
     * @see #appendUser(User)
     */
    public MessageBuilder appendMention(User user) {
        strBuilder.append(user.getMentionTag());
        return this;
    }

    /**
     * Appends an user (@user).
     *
     * @param user The user to append.
     * @return This object to reuse it.
     * @see #appendMention(User)
     */
    public MessageBuilder appendUser(User user) {
        return appendMention(user);
    }

    /**
     * Appends a new line.
     *
     * @return This object to reuse it.
     */
    public MessageBuilder appendNewLine() {
        strBuilder.append("\n");
        return this;
    }

    /**
     * Appends a channel (@channel).
     *
     * @param channel The channel to append.
     * @return This object to reuse it.
     */
    public MessageBuilder appendChannel(Channel channel) {
        strBuilder.append(channel.getMentionTag());
        return this;
    }

    /**
     * Appends a role (@role).
     *
     * @param role The role to append.
     * @return This object to reuse it.
     */
    public MessageBuilder appendRole(Role role) {
        strBuilder.append(role.getMentionTag());
        return this;
    }

    /**
     * Gets the {@link StringBuilder} which is used to build the message.
     *
     * @return The StringBuilder which is used to build the message.
     */
    public StringBuilder getStringBuilder() {
        return strBuilder;
    }

    /**
     * Generates the String to send.
     *
     * @return The String to send.
     */
    public String build() {
        return strBuilder.toString();
    }

    /**
     * Generates the String to send.
     *
     * @return The String to send.
     */
    @Override
    public String toString() {
        return build();
    }
}