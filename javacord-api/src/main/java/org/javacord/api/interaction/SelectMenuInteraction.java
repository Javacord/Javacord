package org.javacord.api.interaction;

import org.javacord.api.entity.message.component.SelectMenuOption;

import java.util.List;
import java.util.Optional;

public interface SelectMenuInteraction extends MessageComponentInteraction {

    /**
     * Get the options the user was chosen.
     *
     * @return The options.
     */
    List<SelectMenuOption> getChosenOptions();

    /**
     * Get all options from the select menu.
     *
     * @return All options.
     */
    List<SelectMenuOption> getPossibleOptions();

    /**
     * Get the custom id of the select menu.
     *
     * @return The custom ID.
     */
    String getCustomId();

    /**
     * Get the placeholder of the select menu.
     *
     * @return The placeholder.
     */
    Optional<String> getPlaceholder();

    /**
     * Gets the minimum amount of options which must be selected.
     *
     * @return The min values.
     */
    int getMinimumValues();

    /**
     * Gets the maximum amount of options which can be selected.
     *
     * @return The max values.
     */
    int getMaximumValues();

}
