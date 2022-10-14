package org.javacord.api.entity.message.component;

import java.util.Arrays;

public enum ComponentType {
    UNKNOWN(-1, false),
    ACTION_ROW(1, false),
    BUTTON(2, false),
    SELECT_MENU_STRING(3, true),
    TEXT_INPUT(4, false),
    SELECT_MENU_USER(5, true),
    SELECT_MENU_ROLE(6, true),
    SELECT_MENU_MENTIONABLE(7, true),
    SELECT_MENU_CHANNEL(8, true);

    private static final ComponentType[] selectMenuTypes = Arrays.stream(ComponentType.values())
            .filter(ComponentType::isSelectMenuType)
            .toArray(ComponentType[]::new);

    private final int value;
    private final boolean selectMenu;

    ComponentType(final int i, final boolean selectMenu) {
        this.value = i;
        this.selectMenu = selectMenu;
    }

    /**
     * Get the internal value.
     *
     * @return The component type's identifier.
     */
    public int value() {
        return this.value;
    }

    /**
     * Whether this component is a type of select menu.
     *
     * @return True if it's a type of select menu.
     */
    public boolean isSelectMenuType() {
        return this.selectMenu;
    }

    /**
     * Gets an array with all types that are text channel types.
     *
     * @return All types that are text channel types.
     */
    public static ComponentType[] getSelectMenuTypes() {
        return selectMenuTypes;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Parse an integer into a ComponentType.
     *
     * @param id A component type identifier.
     * @return A ComponentType enumerator.
     */
    public static ComponentType fromId(int id) {
        for (ComponentType value : values()) {
            if (value.value == id) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
