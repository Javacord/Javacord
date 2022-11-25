package org.javacord.api.entity.message.component;

import java.util.List;

/**
 * This interface represents a select menu that can be edited.
 */
public interface EditableSelectMenu extends SelectMenu {

    /**
     * Sets the placeholder of the select menu.
     *
     * @param placeholder The placeholder of the select menu.
     */
    void setPlaceholder(String placeholder);

    /**
     * Sets the custom id of the select menu.
     *
     * @param customId The custom id of the select menu.
     */
    void setCustomId(String customId);

    /**
     * Sets the minimum number of options that must be chosen.
     *
     * @param minimumValues The minimum number of options that must be chosen.
     */
    void setMinimumValues(int minimumValues);

    /**
     * Sets the maximum number of options that can be chosen.
     *
     * @param maximumValues The maximum number of options that can be chosen.
     */
    void setMaximumValues(int maximumValues);

    /**
     * Sets weather the select menu is disabled.
     *
     * @param disabled Whether the select menu is disabled.
     */
    void setDisabled(boolean disabled);

    /**
     * Sets the options of the select menu.
     *
     * @param options The options of the select menu.
     */
    void setOptions(List<SelectMenuOption> options);
}
