package org.javacord.api.entity.activity;

import org.javacord.api.entity.Icon;
import java.util.Optional;

/**
 * This class represents an activity asset.
 */
public interface ActivityAssets {

    /**
     * Gets the large image value of the asset.
     * If the value is from a different platform like Spotify, YouTube, etc. you should try to get the image from
     * one of their CDNs which can be:
     * <ul>
     *     <li>Spotify: {@code https://i.scdn.co/image/{cover_id}}</li>
     *     <li>Youtube: {@code https://i.ytimg.com/vi/{video_id}/hqdefault_live.jpg}</li>
     *     <li>Twitch: {@code https://static-cdn.jtvnw.net/previews-ttv/live_user_{user_id}.png}</li>
     * </ul>
     * but take these with a grain of salt as they can change at any time.
     *
     * @return The large image value of the asset.
     */
    Optional<String> getLargeImageValue();

    /**
     * Gets the large image of the asset.
     * Always empty for activities of other services like Spotify, YouTube, etc.
     *
     * @return The large image of the asset.
     */
    Optional<Icon> getLargeImage();

    /**
     * Gets the large text of the asset.
     *
     * @return The large text of the asset.
     */
    Optional<String> getLargeText();

    /**
     * Gets the small image value of the asset.
     * If the value is from a different platform like Spotify, Youtube, etc. you should try to get the image from
     * one of their CDNs which can be:
     * <ul>
     *     <li>Spotify: {@code https://i.scdn.co/image/{cover_id}}</li>
     *     <li>Youtube: {@code https://i.ytimg.com/vi/{video_id}/hqdefault_live.jpg}</li>
     *     <li>Twitch: {@code https://static-cdn.jtvnw.net/previews-ttv/live_user_{user_id}.png}</li>
     * </ul>
     * but take these with a grain of salt as they can change at any time.
     *
     * @return The small image value of the asset.
     */
    Optional<String> getSmallImageValue();

    /**
     * Gets the small image of the asset.
     * Always empty for activities of other services like Spotify, YouTube, etc.
     *
     * @return The small image of the asset.
     */
    Optional<Icon> getSmallImage();

    /**
     * Gets the small text of the asset.
     *
     * @return The small text of the asset.
     */
    Optional<String> getSmallText();

}
