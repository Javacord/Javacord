package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;

import java.util.Optional;

/**
 * This interface represents a Button component.
 */
public interface Button extends LowLevelComponent {
    /**
     * Get the button's style.
     *
     * @return The button's style.
     */
    ButtonStyle getStyle();

    /**
     * Get the button's identifier.
     *
     * @return The button's custom identifier.
     */
    Optional<String> getCustomId();

    /**
     * Get the button's label.
     *
     * @return The button's label.
     */
    Optional<String> getLabel();

    /**
     * Get the button's clickable URL.
     *
     * @return The button's URL.
     */
    Optional<String> getUrl();

    /**
     * Get whether or not the button is disabled.
     *
     * @return Whether or not the button is disabled.
     */
    Optional<Boolean> isDisabled();

    /**
     * Get the emoji button's emoji.
     *
     * @return The emoji button's emoji.
     */
    Optional<Emoji> getEmoji();
}
