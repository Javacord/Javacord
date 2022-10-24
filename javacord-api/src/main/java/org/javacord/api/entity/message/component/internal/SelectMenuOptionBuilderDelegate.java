package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.SelectMenuOption;

public interface SelectMenuOptionBuilderDelegate {

    /**
     * Copy the give select menu option.
     *
     * @param selectMenuOption The select menu option.
     */
    void copy(SelectMenuOption selectMenuOption);

    /**
     * Set the label of the select menu.
     *
     * @param label The label.
     */
    void setLabel(String label);

    /**
     * Set the value of the select menu.
     *
     * @param value The value.
     */
    void setValue(String value);

    /**
     * Set the description of the select menu.
     *
     * @param description The description.
     */
    void setDescription(String description);

    /**
     * Set if the option is the default option.
     *
     * @param isDefault Is default.
     */
    void setDefault(boolean isDefault);

    /**
     * Sets the emoji for the option.
     *
     * @param unicode The emoji as a unicode string.
     */
    void setEmoji(String unicode);

    /**
     * Set the emoji for the option.
     *
     * @param emoji The emoji.
     */
    void setEmoji(Emoji emoji);

    /**
     * Build the select menu option.
     *
     * @return The option.
     */
    SelectMenuOption build();
}
