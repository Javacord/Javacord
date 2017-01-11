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

import de.btobastian.javacord.entities.message.MessageAttachment;
import de.btobastian.javacord.utils.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of MessageAttachment.
 */
public class ImplMessageAttachment implements MessageAttachment {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplMessageAttachment.class);

    private final String url;
    private final String proxyUrl;
    private final int size;
    private final String id;
    private final String name;

    /**
     * Creates a new instance of this class.
     *
     * @param url The url of the attachment.
     * @param proxyUrl The proxied url of the attachment.
     * @param size The size of the attachment.
     * @param id The id of the attachment.
     * @param name The name of the attachment.
     */
    public ImplMessageAttachment(String url, String proxyUrl, int size, String id, String name) {
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.size = size;
        this.id = id;
        this.name = name;
    }

    @Override
    public URL getUrl() {
        try {
            return url == null ? null : new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Malformed url of message attachment! Please contact the developer!", e);
        }
        return null;
    }

    @Override
    public URL getProxyUrl() {
        try {
            return proxyUrl == null ? null : new URL(proxyUrl);
        } catch (MalformedURLException e) {
            logger.warn("Malformed proxy url of message attachment! Please contact the developer!", e);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public String toString() {
        return getFileName() + " (id: " + getId() + ", url: " + getUrl() + ")";
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
