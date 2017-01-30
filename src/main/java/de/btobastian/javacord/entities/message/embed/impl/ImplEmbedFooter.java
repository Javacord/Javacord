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
package de.btobastian.javacord.entities.message.embed.impl;

import de.btobastian.javacord.entities.message.embed.EmbedFooter;
import de.btobastian.javacord.utils.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of the embed footer interface.
 */
public class ImplEmbedFooter implements EmbedFooter {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbedFooter.class);

    private String text;
    private String iconUrl;
    private String proxyIconUrl;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     */
    public ImplEmbedFooter(JSONObject data) {
        text = data.has("text") ? data.getString("text") : null;
        iconUrl = data.has("icon_url") ? data.getString("icon_url") : null;
        proxyIconUrl = data.has("proxy_icon_url") ? data.getString("proxy_icon_url") : null;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public URL getIconUrl() {
        if (iconUrl == null) {
            return null;
        }
        try {
            return new URL(iconUrl);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the icon url of the embed footer is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public URL getProxyIconUrl() {
        if (proxyIconUrl == null) {
            return null;
        }
        try {
            return new URL(proxyIconUrl);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the proxy icon url of the embed footer is malformed! Please contact the developer!", e);
            return null;
        }
    }

}
