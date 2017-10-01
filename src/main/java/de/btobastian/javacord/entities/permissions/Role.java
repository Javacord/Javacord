package de.btobastian.javacord.entities.permissions;

import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.entities.Server;

import java.awt.*;
import java.util.Optional;

/**
 * This class represents a Discord role, e.g. "moderator".
 */
public interface Role extends DiscordEntity, Mentionable {

    /**
     * Gets the server of the role.
     *
     * @return The server of the role.
     */
    Server getServer();

    /**
     * Gets the name of the role.
     *
     * @return The name of the role.
     */
    String getName();

    /**
     * Gets the position of the role.
     *
     * @return The position of the role.
     */
    int getPosition();

    /**
     * Gets the color of the role.
     *
     * @return The color of the role.
     */
    Optional<Color> getColor();

    /**
     * Check if this role is pinned in the user listing (sometimes called "hoist").
     *
     * @return Whether this role is pinned in the user listing or not.
     */
    boolean isDisplayedSeparately();

    @Override
    default String getMentionTag() {
        return "<@&" + getId() + ">";
    }

}
