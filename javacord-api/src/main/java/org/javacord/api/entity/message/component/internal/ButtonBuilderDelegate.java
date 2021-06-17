package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;

public interface ButtonBuilderDelegate extends ComponentBuilderDelegate {
    /**
     * Get the button's type.
     *
     * @return Always {@link ComponentType#BUTTON}
     */
    ComponentType getType();

    /**
     * Copy a button's values into the builder.
     *
     * @param button The button to copy.
     */
    void copy(Button button);

    /**
     * Set the button's emoji to a custom emoji.
     *
     * @param emoji The custom emoji.
     */
    void setEmoji(CustomEmoji emoji);

    /**
     * Set the button's emoji based on a unicode character.
     *
     * @param unicode The unicode emoji character.
     */
    void setEmoji(String unicode);

    /**
     * Set the button's emoji.
     *
     * @param emoji The button's emoji.
     */
    void setEmoji(Emoji emoji);

    /**
     * Get the button's style.
     *
     * @return The button's style.
     */
    ButtonStyle getStyle();

    /**
     * Get the button's label.
     *
     * @return The button's label.
     */
    String getLabel();

    /**
     * Get the button's component identifier.
     *
     * @return The button's component identifier.
     */
    String getCustomId();

    /**
     * Get the button's clickable URL.
     *
     * @return The button's clickable URL.
     */
    String getUrl();

    /**
     * Get whether or not the button is disabled.
     *
     * @return Whether or not the button is disabled.
     */
    Boolean isDisabled();

    /**
     * Get the button's emoji.
     *
     * @return The button's emoji.
     */
    Emoji getEmoji();

    /**
     * Set the button's style.
     *
     * @param style The style of the button.
     */
    void setStyle(ButtonStyle style);

    /**
     * Set the button's label.
     *
     * @param label The button's label.
     */
    void setLabel(String label);

    /**
     * Set the button's custom ID.
     *
     * @param customId The button's identifier.
     */
    void setCustomId(String customId);

    /**
     * Set the button's URL.
     *
     * @param url The button's clickable URL.
     */
    void setUrl(String url);

    /**
     * Set the button to disabled.
     *
     * @param isDisabled Whether the button is disabled or not.
     */
    void setDisabled(Boolean isDisabled);

    /**
     * Creates a {@link Button} instance with the given values.
     *
     * @return The created button instance.
     */
    Button build();
}
