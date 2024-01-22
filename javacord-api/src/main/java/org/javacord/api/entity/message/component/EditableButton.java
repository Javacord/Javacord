package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;

/**
 * This interface represents a button that can be edited.
 */
public interface EditableButton extends Button {

    /**
     * Sets the style of the button.
     *
     * @param style The style of the button.
     */
    void setStyle(ButtonStyle style);

    /**
     * Sets the custom id of the button.
     *
     * @param customId The custom id of the button.
     */
    void setCustomId(String customId);

    /**
     * Sets the label of the button.
     *
     * @param label The label of the button.
     */
    void setLabel(String label);

    /**
     * Sets the url of the button.
     *
     * @param url The emoji of the button.
     */
    void setUrl(String url);

    /**
     * Sets weather the button is disabled.
     *
     * @param disabled Whether the button is disabled.
     */
    void setDisabled(boolean disabled);

    /**
     * Sets the emoji of the button.
     *
     * @param emoji The emoji of the button.
     */
    void setEmoji(Emoji emoji);
}
