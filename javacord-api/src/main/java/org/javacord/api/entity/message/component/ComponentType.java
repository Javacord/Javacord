package org.javacord.api.entity.message.component;

public enum ComponentType {
    UNKNOWN(-1),
    ACTION_ROW(1),
    BUTTON(2),
    SELECT_MENU(3),
    TEXT_INPUT(4);

    private final int value;

    ComponentType(int i) {
        this.value = i;
    }

    /**
     * Get the internal value.
     *
     * @return The component type's identifier.
     */
    public int value() {
        return this.value;
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
