package org.javacord.api.entity.message.component;

import org.javacord.api.entity.channel.ChannelType;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface SelectMenu extends LowLevelComponent {

    /**
     * Get the channel types of this select menu if it's of type {@link ComponentType#SELECT_MENU_CHANNEL}.
     *
     * @return The channel types if this select menu is restricted to any.
     */
    EnumSet<ChannelType> getChannelTypes();

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
     * @param options  The select menu options.
     * @return The created select menu.
     * @deprecated Use {@link SelectMenu#createStringMenu(String, List)} instead.
     */
    @Deprecated
    static SelectMenu create(String customId, List<SelectMenuOption> options) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId   The custom ID for the select menu.
     * @param options    The select menu options.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     * @deprecated Use {@link SelectMenu#createStringMenu(String, List, boolean)} instead.
     */
    @Deprecated
    static SelectMenu create(String customId, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param options     The select menu options.
     * @return The created select menu.
     * @deprecated Use {@link SelectMenu#createStringMenu(String, String, List)} )} instead.
     */
    @Deprecated
    static SelectMenu create(String customId, String placeholder, List<SelectMenuOption> options) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param options     The select menu options.
     * @param isDisabled  Set if the menu should be disabled.
     * @return The created select menu.
     * @deprecated Use {@link SelectMenu#createStringMenu(String, String, List, boolean)} instead.
     */
    @Deprecated
    static SelectMenu create(String customId, String placeholder, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param options       The select menu options.
     * @return The created select menu.
     * @deprecated Use {@link SelectMenu#createStringMenu(String, String, int, int, List)} instead.
     */
    @Deprecated
    static SelectMenu create(String customId, String placeholder, int minimumValues,
                             int maximumValues, List<SelectMenuOption> options) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param options       The select menu options.
     * @param isDisabled    Set if the menu should be disabled.
     * @return The created select menu.
     * @deprecated Use {@link SelectMenu#createStringMenu(String, String, int, int, List, boolean)} instead.
     */
    @Deprecated
    static SelectMenu create(String customId, String placeholder, int minimumValues,
                             int maximumValues, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @param options  The select menu options.
     * @return The created select menu.
     */
    static SelectMenu createStringMenu(String customId, List<SelectMenuOption> options) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId   The custom ID for the select menu.
     * @param options    The select menu options.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createStringMenu(String customId, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param options     The select menu options.
     * @return The created select menu.
     */
    static SelectMenu createStringMenu(String customId, String placeholder, List<SelectMenuOption> options) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param options     The select menu options.
     * @param isDisabled  Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createStringMenu(String customId, String placeholder, List<SelectMenuOption> options,
                                       boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param options       The select menu options.
     * @return The created select menu.
     */
    static SelectMenu createStringMenu(String customId, String placeholder, int minimumValues,
                                       int maximumValues, List<SelectMenuOption> options) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addOptions(options)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param options       The select menu options.
     * @param isDisabled    Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createStringMenu(String customId, String placeholder, int minimumValues,
                                       int maximumValues, List<SelectMenuOption> options, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_STRING, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addOptions(options)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId     The custom ID for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId).build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId     The custom ID for the select menu.
     * @param channelTypes The channel types which should be available in the select menu.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId, Iterable<ChannelType> channelTypes) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId)
                .addChannelTypes(channelTypes)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId     The custom ID for the select menu.
     * @param channelTypes The channel types which should be available in the select menu.
     * @param isDisabled   Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId, Iterable<ChannelType> channelTypes, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId)
                .addChannelTypes(channelTypes)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId     The custom ID for the select menu.
     * @param placeholder  The placeholder for the select menu.
     * @param channelTypes The channel types which should be available in the select menu.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId, String placeholder, Iterable<ChannelType> channelTypes) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId)
                .setPlaceholder(placeholder)
                .addChannelTypes(channelTypes)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId     The custom ID for the select menu.
     * @param placeholder  The placeholder for the select menu.
     * @param channelTypes The channel types which should be available in the select menu.
     * @param isDisabled   Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId, String placeholder, Iterable<ChannelType> channelTypes,
                                        boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId)
                .setPlaceholder(placeholder)
                .addChannelTypes(channelTypes)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param channelTypes  The channel types which should be available in the select menu.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId, String placeholder, int minimumValues,
                                        int maximumValues, Iterable<ChannelType> channelTypes) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addChannelTypes(channelTypes)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param channelTypes  The channel types which should be available in the select menu.
     * @param isDisabled    Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createChannelMenu(String customId, String placeholder, int minimumValues,
                                        int maximumValues, Iterable<ChannelType> channelTypes, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_CHANNEL, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .addChannelTypes(channelTypes)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createRoleMenu(String customId) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_ROLE, customId).build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId   The custom ID for the select menu.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createRoleMenu(String customId, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_ROLE, customId)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createRoleMenu(String customId, String placeholder) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_ROLE, customId)
                .setPlaceholder(placeholder)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param isDisabled  Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createRoleMenu(String customId, String placeholder, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_ROLE, customId)
                .setPlaceholder(placeholder)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @return The created select menu.
     */
    static SelectMenu createRoleMenu(String customId, String placeholder, int minimumValues, int maximumValues) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_ROLE, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param isDisabled    Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createRoleMenu(String customId, String placeholder, int minimumValues,
                                     int maximumValues, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_ROLE, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createMentionableMenu(String customId) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_MENTIONABLE, customId).build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId   The custom ID for the select menu.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createMentionableMenu(String customId, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_MENTIONABLE, customId)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createMentionableMenu(String customId, String placeholder) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_MENTIONABLE, customId)
                .setPlaceholder(placeholder)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param isDisabled  Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createMentionableMenu(String customId, String placeholder, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_MENTIONABLE, customId)
                .setPlaceholder(placeholder)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @return The created select menu.
     */
    static SelectMenu createMentionableMenu(String customId, String placeholder, int minimumValues, int maximumValues) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_MENTIONABLE, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param isDisabled    Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createMentionableMenu(String customId, String placeholder, int minimumValues,
                                     int maximumValues, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_MENTIONABLE, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId The custom ID for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createUserMenu(String customId) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_USER, customId).build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId   The custom ID for the select menu.
     * @param isDisabled Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createUserMenu(String customId, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_USER, customId)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @return The created select menu.
     */
    static SelectMenu createUserMenu(String customId, String placeholder) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_USER, customId)
                .setPlaceholder(placeholder)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId    The custom ID for the select menu.
     * @param placeholder The placeholder for the select menu.
     * @param isDisabled  Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createUserMenu(String customId, String placeholder, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_USER, customId)
                .setPlaceholder(placeholder)
                .setDisabled(isDisabled)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @return The created select menu.
     */
    static SelectMenu createUserMenu(String customId, String placeholder, int minimumValues, int maximumValues) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_USER, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .build();
    }

    /**
     * Creates a new select menu with the given values.
     *
     * @param customId      The custom ID for the select menu.
     * @param placeholder   The placeholder for the select menu
     * @param minimumValues The minimum amount of options which must be selected.
     * @param maximumValues The maximum amount of options which can be selected.
     * @param isDisabled    Set if the menu should be disabled.
     * @return The created select menu.
     */
    static SelectMenu createUserMenu(String customId, String placeholder, int minimumValues,
                                            int maximumValues, boolean isDisabled) {
        return new SelectMenuBuilder(ComponentType.SELECT_MENU_USER, customId)
                .setPlaceholder(placeholder)
                .setMinimumValues(minimumValues)
                .setMaximumValues(maximumValues)
                .setDisabled(isDisabled)
                .build();
    }

}
