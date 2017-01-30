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
package de.btobastian.javacord.entities.message.embed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class is used to create embeds.
 */
public class EmbedBuilder {

    private String title = null;
    private String description = null;
    private String url = null;
    private Color color = null;

    // Footer
    private String footerText = null;
    private String footerIconUrl = null;

    // Image
    private String imageUrl = null;

    // Author
    private String authorName = null;
    private String authorUrl = null;
    private String authorIconUrl = null;

    // Thumbnail
    private String thumbnailUrl = null;

    // Fields
    // (Array indices: 0: name (String), 1: value (String), 2: inline (boolean)
    private List<Object[]> fields = new ArrayList<>();

    /**
     * Class constructor.
     */
    public EmbedBuilder() {
        // Default constructor
    }

    /**
     * Sets the title of the embed.
     *
     * @param title The title of the embed.
     * @return This object to reuse it.
     */
    public EmbedBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the description of the embed.
     *
     * @param description The description of the embed.
     * @return This object to reuse it.
     */
    public EmbedBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the url of the embed.
     *
     * @param url The url of the embed.
     * @return This object to reuse it.
     */
    public EmbedBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the color of the embed.
     *
     * @param color The color of the embed.
     * @return This object to reuse it.
     */
    public EmbedBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @return This object to reuse it.
     */
    public EmbedBuilder setFooter(String text) {
        return setFooter(text, null);
    }

    /**
     * Sets the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The url of the footer's icon.
     * @return This object to reuse it.
     */
    public EmbedBuilder setFooter(String text, String iconUrl) {
        footerText = text;
        footerIconUrl = iconUrl;
        return this;
    }

    /**
     * Sets the image of the embed.
     *
     * @param url The url of the image.
     * @return This object to reuse it.
     */
    public EmbedBuilder setImage(String url) {
        imageUrl = url;
        return this;
    }

    /**
     * Sets the author if the embed.
     *
     * @param name The name of the author.
     * @return This object to reuse it.
     */
    public EmbedBuilder setAuthor(String name) {
        return setAuthor(name, null, null);
    }

    /**
     * Sets the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param iconUrl The url of the author's icon.
     * @return This object to reuse it.
     */
    public EmbedBuilder setAuthor(String name, String url, String iconUrl) {
        authorName = name;
        authorUrl = url;
        authorIconUrl = iconUrl;
        return this;
    }

    /**
     * Sets the thumbnail of the embed.
     *
     * @param url The url of the thumbnail.
     * @return This object to reuse it.
     */
    public EmbedBuilder setThumbnail(String url) {
        thumbnailUrl = url;
        return this;
    }

    /**
     * Adds a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline or not.
     * @return This object to reuse it.
     */
    public EmbedBuilder addField(String name, String value, boolean inline) {
        fields.add(new Object[]{name, value, inline});
        return this;
    }

    /**
     * Gets the embed as a {@link JSONObject}. This is what is sent to Discord.
     *
     * @return The embed as a JSONObject.
     */
    public JSONObject toJSONObject() {
        JSONObject object = new JSONObject();
        object.put("type", "rich");
        if (title != null) {
            object.put("title", title);
        }
        if (description != null) {
            object.put("description", description);
        }
        if (url != null) {
            object.put("url", url);
        }
        if (color != null) {
            object.put("color", color.getRGB() & 0xFFFFFF);
        }
        if (footerText != null) {
            JSONObject footer = new JSONObject();
            footer.put("text", footerText);
            if (footerIconUrl != null) {
                footer.put("icon_url", footerIconUrl);
            }
            object.put("footer", footer);
        }
        if (imageUrl != null) {
            object.put("image", new JSONObject().put("url", imageUrl));
        }
        if (authorName != null) {
            JSONObject author = new JSONObject();
            author.put("name", authorName);
            if (authorUrl != null) {
                author.put("url", authorUrl);
            }
            if (authorIconUrl != null) {
                author.put("icon_url", authorIconUrl);
            }
            object.put("author", author);
        }
        if (thumbnailUrl != null) {
            object.put("thumbnail", new JSONObject().put("url", thumbnailUrl));
        }
        if (fields.size() > 0) {
            JSONArray jsonFields = new JSONArray();
            for (Object[] field : fields) {
                JSONObject jsonField = new JSONObject();
                if (field[0] != null) {
                    jsonField.put("name", field[0]);
                }
                if (field[1] != null) {
                    jsonField.put("value", field[1]);
                }
                if (field[2] != null) {
                    jsonField.put("inline", (boolean) field[2]);
                }
                jsonFields.put(jsonField);
            }
            object.put("fields", jsonFields);
        }
        return object;
    }

}
