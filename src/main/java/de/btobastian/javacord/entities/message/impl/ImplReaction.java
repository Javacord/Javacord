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
package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.entities.CustomEmoji;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Reaction;
import org.apache.http.concurrent.FutureCallback;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Future;

/**
 * The implementation of the Reaction interface.
 */
public class ImplReaction implements Reaction {

    private final Message message;
    private int count;
    private boolean usedByYou;
    private final CustomEmoji customEmoji;
    private final String unicodeEmoji;

    /**
     * Class constructor.
     *
     * @param message The message of the reaction.
     * @param data The data of the reaction.
     */
    public ImplReaction(Message message, JSONObject data) {
        this.message = message;
        this.count = data.getInt("count");
        this.usedByYou = data.getBoolean("me");

        JSONObject emoji = data.getJSONObject("emoji");
        if (emoji.isNull("id")) {
            customEmoji = null;
            unicodeEmoji = emoji.getString("name");
        } else {
            unicodeEmoji = null;
            customEmoji = message.getChannelReceiver().getServer().getCustomEmojiById(emoji.getString("id"));
        }
    }

    /**
     * Class constructor.
     *
     * @param message The message of the reaction.
     * @param usedByYou If the reaction is used by you.
     * @param count The count of the reaction.
     * @param unicodeEmoji The unicode emoji or null.
     * @param customEmoji The custom emoji or null.
     */
    public ImplReaction(Message message, boolean usedByYou, int count, String unicodeEmoji, CustomEmoji customEmoji) {
        this.message = message;
        this.count = count;
        this.usedByYou = usedByYou;
        this.customEmoji = customEmoji;
        this.unicodeEmoji = unicodeEmoji;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isUsedByYou() {
        return usedByYou;
    }

    @Override
    public boolean isCustomEmoji() {
        return customEmoji != null;
    }

    @Override
    public boolean isUnicodeEmoji() {
        return unicodeEmoji != null;
    }

    @Override
    public CustomEmoji getCustomEmoji() {
        return customEmoji;
    }

    @Override
    public String getUnicodeEmoji() {
        return unicodeEmoji;
    }

    @Override
    public Future<List<User>> getReactors() {
        return getReactors(null);
    }

    @Override
    public Future<List<User>> getReactors(FutureCallback<List<User>> callback) {
        return null;
    }

    @Override
    public Future<Void> removeReactor(User user) {
        return null;
    }

    /**
     * Increments the count.
     */
    public void incrementCount(boolean you) {
        count++;
        usedByYou = you || usedByYou;
    }

    /**
     * Decrements the count.
     */
    public void decrementCount(boolean you) {
        count--;
        usedByYou = !you && usedByYou;
    }

    @Override
    public String toString() {
        return isUnicodeEmoji() ? getUnicodeEmoji() : getCustomEmoji().toString();
    }
}
