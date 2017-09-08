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
package de.btobastian.javacord.impl;

import de.btobastian.javacord.AccountType;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.utils.ThreadPool;
import de.btobastian.javacord.utils.ratelimits.RatelimitManager;

public class ImplDiscordApi implements DiscordApi {

    /**
     * The thread pool which is used internally.
     */
    private final ThreadPool threadPool = new ThreadPool();

    /**
     * The ratelimit manager for this bot.
     */
    private final RatelimitManager ratelimitManager = new RatelimitManager();

    /**
     * The account type of the bot.
     */
    private final AccountType accountType;

    /**
     * The token used for authentication.
     */
    private String token;

    public ImplDiscordApi(AccountType accountType, String token) {
        this.accountType = accountType;
        this.token = accountType.getTokenPrefix() + token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    @Override
    public RatelimitManager getRatelimitManager() {
        return ratelimitManager;
    }

}
