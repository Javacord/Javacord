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

import java.awt.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;

/**
 * This interface represents an embed.
 */
public interface Embed {

    /**
     * Gets the title of the embed.
     *
     * @return The title of the embed.
     */
    public String getTitle();

    /**
     * Gets the type of the embed.
     * (always "rich" for webhook embeds)
     *
     * @return The type of the embed.
     */
    public String getType();

    /**
     * Gets the description of the embed.
     *
     * @return The description of the embed.
     */
    public String getDescription();

    /**
     * Gets the url of the embed.
     *
     * @return The url of the embed.
     */
    public URL getUrl();

    /**
     * Gets the date of creation.
     *
     * @return The date of creation.
     */
    public Calendar getCreationDate();

    /**
     * Gets the color of the embed.
     *
     * @return The color of the embed.
     */
    public Color getColor();

    /**
     * Gets the footer of the embed.
     *
     * @return The footer of the embed.
     */
    public EmbedFooter getFooter();

    /**
     * Gets the image of the embed.
     *
     * @return The image of the embed.
     */
    public EmbedImage getImage();

    /**
     * Gets the thumbnail of the embed.
     *
     * @return The thumbnail of the embed.
     */
    public EmbedThumbnail getThumbnail();

    /**
     * Gets the video of the embed.
     *
     * @return The video of the embed.
     */
    public EmbedVideo getVideo();

    /**
     * Gets the provider of the embed.
     *
     * @return The provider of the embed.
     */
    public EmbedProvider getProvider();

    /**
     * Gets the author of the embed.
     *
     * @return The author of the embed.
     */
    public EmbedAuthor getAuthor();

    /**
     * Gets the fields of the embed.
     *
     * @return The fields of the embed.
     */
    public Collection<EmbedField> getFields();

}
