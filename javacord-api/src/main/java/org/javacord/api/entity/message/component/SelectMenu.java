package org.javacord.api.entity.message.component;

import java.util.List;
import java.util.Optional;

public interface SelectMenu extends LowLevelComponent {

    /**
     * Get the select menu's placeholder id.
     *
     * @return The select menu's placeholder id.
     */
    Optional<String> getPlaceholder();

    /**
     * Get the select menu's custom id.
     *
     * @return The select menu's custom id.
     */
    String getCustomId();

    /**
     * Gets the minimum amount of options which must be selected.
     *
     * @return The select menu's minimum values.
     */
    int getMinimumValues();

    /**
     * Gets the maximum amount of options which can be selected.
     *
     * @return The select menu's maximum values.
     */
    int getMaximumValues();

    /**
     * Get the select menu's options.
     *
     * @return The select menu's options.
     */
    List<SelectMenuOption> getOptions();

    /**
     * If the select menu is disabled.
     *
     * @return Is disabled.
     */
    boolean isDisabled();

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param options The select menu options.
     * @return The created select menu.
     */
    static SelectMenu create(String customId, List<SelectMenuOption> options) {
        return new SelectMenuBuilder()
                .setCustomId(customId)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param options The select menu options.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu create(String customId, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder()
                .setCustomId(customId)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param options The select menu options.
     * @return The created select menu.
     */
    static SelectMenu create(String customId, String placeholder, List<SelectMenuOption> options) {
        return new SelectMenuBuilder()
                .setCustomId(customId)
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param options The select menu options.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu create(String customId, String placeholder, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder()
                .setCustomId(customId)
                .setPlaceholder(placeholder)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param options The select menu options.
     * @return The created select menu.
     */
    static SelectMenu create(String customId, String placeholder, int minimumValues,
                             int maximumValues, List<SelectMenuOption> options) {
        return new SelectMenuBuilder()
                .setCustomId(customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param options The select menu options.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu create(String customId, String placeholder, int minimumValues,
                             int maximumValues, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder()
                .setCustomId(customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

}
