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
package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.entities.message.MessageAttachment;

import java.net.URL;

/**
 * The implementation of MessageAttachment.
 */
public class ImplMessageAttachment implements MessageAttachment {

    private String url = null;
    private String proxyUrl = null;
    private int size = -1;
    private String id = null;
    private String name = null;

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
        return null;
    }

    @Override
    public URL getProxyUrl() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

}
