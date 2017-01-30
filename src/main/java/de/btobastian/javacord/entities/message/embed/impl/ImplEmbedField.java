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

import de.btobastian.javacord.entities.message.embed.EmbedField;
import org.json.JSONObject;

/**
 * The implementation of the embed field interface.
 */
public class ImplEmbedField implements EmbedField {

    private String name;
    private String value;
    private boolean inline;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     */
    public ImplEmbedField(JSONObject data) {
        name = data.has("name") ? data.getString("name") : null;
        value = data.has("value") ? data.getString("value") : null;
        inline = data.has("inline") && data.getBoolean("inline");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInline() {
        return inline;
    }
}
