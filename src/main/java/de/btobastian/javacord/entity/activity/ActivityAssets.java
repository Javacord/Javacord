package de.btobastian.javacord.entity.activity;

import de.btobastian.javacord.entity.Icon;

import java.util.Optional;

/**
 * This class represents a activity asset.
 */
public interface ActivityAssets {

    /**
     * Gets the large image of the asset.
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
     * Gets the small image of the asset.
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
