package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;

public interface SelectMenuBuilderDelegate extends ComponentBuilderDelegate {

    /**
     * Get the select menu's type.
     *
     * @return The specific type of the select menu.
     */
    ComponentType getType();

    /**
     * Sets the type of this select menu.
     *
     * @param type The type of the select menu.
     */
    void setType(ComponentType type);

    /**
     * Copy a select menu's values into the builder.
     *
     * @param selectMenu The select menu to copy.
     */
    void copy(SelectMenu selectMenu);

    /**
     * Add a channel type to the select menu.
     *
     * @param channelType The channel type to add.
     */
    void addChannelType(ChannelType channelType);

    /**
     * Add an option to the select menu.
     *
     * @param selectMenuOption The option to add.
     */
    void addOption(SelectMenuOption selectMenuOption);

    /**
     * Remove an option from the select menu.
     *
     * @param selectMenuOption The option to remove.
     */
    void removeOption(SelectMenuOption selectMenuOption);

    /**
     * Set the select menu's placeholder.
     *
     * @param placeholder The select menu's placeholder.
     */
    void setPlaceholder(String placeholder);

    /**
     * Set the select menu's component identifier.
     *
     * @param customId The select menu's identifier.
     */
    void setCustomId(String customId);

    /**
     * Set the min amount of options to choose.
     *
     * @param minimumValues The select menu's minimum values
     */
    void setMinimumValues(int minimumValues);

    /**
     * Set the max amount of options to choose.
     *
     * @param maximumValues The select menu's maximum values
     */
    void setMaximumValues(int maximumValues);

    /**
     * Set if the select menu should be disabled.
     *
     * @param disabled Is disabled.
     */
    void setDisabled(boolean disabled);

    /**
     * Creates a {@link SelectMenu} instance with the given values.
     *
     * @return The created select menu instance.
     */
    SelectMenu build();

    /**
     * Removes all options from the select menu.
     */
    void removeAllOptions();

    /**
     * Get the custom ID of the select menu.
     *
     * @return The custom ID.
     */
    String getCustomId();
}
