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

import com.google.common.base.Joiner;
import de.btobastian.javacord.entities.message.embed.*;
import de.btobastian.javacord.utils.LoggerUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

/**
 * The implementation of the embed interface.
 */
public class ImplEmbed implements Embed {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(ImplEmbed.class);

    private static final ThreadLocal<SimpleDateFormat> TIMEZONE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> FORMAT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> FORMAT_ALTERNATIVE = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }
    };
    private static final ThreadLocal<SimpleDateFormat> FORMAT_ALTERNATIVE_TWO = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        }
    };

    private String title;
    private String type;
    private String description;
    private String url;
    private Calendar creationDate;
    private Color color;
    private EmbedFooter footer;
    private EmbedImage image;
    private EmbedThumbnail thumbnail;
    private EmbedVideo video;
    private EmbedProvider provider;
    private EmbedAuthor author;
    private Collection<EmbedField> fields;

    /**
     * Creates a new instance of this class.
     *
     * @param data A JSONObject containing all necessary data.
     */
    public ImplEmbed(JSONObject data) {
        title = data.has("title") ? data.getString("title") : null;
        type = data.has("type") ? data.getString("type") : null;
        description = data.has("description") ? data.getString("description") : null;
        url = data.has("url") ? data.getString("url") : null;
        // We all love how easy some things are in Java 7 ._.
        if (data.has("timestamp")) {
            String time = data.getString("timestamp");
            Calendar calendar = Calendar.getInstance();
            try {
                //remove the nano seconds, rejoining on +. If the formatting changes then the string will remain the same
                String nanoSecondsRemoved = Joiner.on("+").join(time.split("\\d{3}\\+"));
                calendar.setTime(TIMEZONE_FORMAT.get().parse(nanoSecondsRemoved));
            } catch (ParseException timeZoneIgnored) {
                try {
                    calendar.setTime(FORMAT.get().parse(time.substring(0, time.length() - 9)));
                } catch (ParseException ignored) {
                    try {
                        calendar.setTime(FORMAT_ALTERNATIVE.get().parse(time.substring(0, time.length() - 9)));
                    } catch (ParseException ignored2) {
                        try {
                            calendar.setTime(FORMAT_ALTERNATIVE_TWO.get().parse(time.substring(0, time.length() - 9)));
                        } catch (ParseException e) {
                            logger.warn("Could not parse timestamp {}. Please contact the developer!", time, e);
                        }
                    }
                }
            }
            creationDate = calendar;
        }
        color = data.has("color") ? new Color(data.getInt("color")) : null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public URL getUrl() {
        if (url == null) {
            return null;
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the embed is malformed! Please contact the developer!", e);
            return null;
        }
    }

    @Override
    public Calendar getCreationDate() {
        return creationDate;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public EmbedFooter getFooter() {
        return footer;
    }

    @Override
    public EmbedImage getImage() {
        return image;
    }

    @Override
    public EmbedThumbnail getThumbnail() {
        return thumbnail;
    }

    @Override
    public EmbedVideo getVideo() {
        return video;
    }

    @Override
    public EmbedProvider getProvider() {
        return provider;
    }

    @Override
    public EmbedAuthor getAuthor() {
        return author;
    }

    @Override
    public Collection<EmbedField> getFields() {
        return fields;
    }
}
