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

import de.btobastian.javacord.entities.message.embed.EmbedAuthor;
import de.btobastian.javacord.utils.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of the embed author interface.
 */
public class ImplEmbedAuthor implements EmbedAuthor {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbedAuthor.class);

    private String name;
    private String url;
    private String iconUrl;
    private String proxyIconUrl;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     */
    public ImplEmbedAuthor(JSONObject data) {
        name = data.has("name") ? data.getString("name") : null;
        url = data.has("url") ? data.getString("url") : null;
        iconUrl = data.has("icon_url") ? data.getString("icon_url") : null;
        proxyIconUrl = data.has("proxy_icon_url") ? data.getString("proxy_icon_url") : null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public URL getUrl() {
        if (url == null) {
            return null;
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed author is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public URL getIconUrl() {
        if (iconUrl == null) {
            return null;
        }
        try {
            return new URL(iconUrl);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the icon url of the embed author is malformed! Please contact the developer!", e);
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
            logger.warn("Seems like the proxy icon url of the embed author is malformed! Please contact the developer!", e);
            return null;
        }
    }

}
