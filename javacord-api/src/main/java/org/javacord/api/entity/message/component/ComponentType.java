package org.javacord.api.entity.message.component;

public enum ComponentType {
    UNKNOWN(-1),
    ACTION_ROW(1),
    BUTTON(2);

    private final int data;

    ComponentType(int i) {
        this.data = i;
    }

    /**
     * Get the internal value.
     *
     * @return The component type's identifier.
     */
    public int value() {
        return this.data;
    }

    @Override
    public String toString() {
        return String.valueOf(this.data);
    }

    /**
     * Parse an integer into a ComponentType.
     *
     * @param identifier A component type identifier.
     * @return A ComponentType enumerator.
     */
    public static ComponentType fromId(int identifier) {
        if (identifier == 1) {
            return ACTION_ROW;
        } else if (identifier == 2) {
            return BUTTON;
        }
        return UNKNOWN;
    }
}
