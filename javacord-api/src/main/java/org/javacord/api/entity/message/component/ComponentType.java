package org.javacord.api.entity.message.component;

public enum ComponentType {
    ActionRow (1),
    Button (2),
    ;

    private final int data;

    ComponentType(int i) {
        this.data = i;
    }

    public int value() {
        return this.data;
    }

    public static ComponentType devalue(int i) {
        if (i == 1) {
            return ActionRow;
        } else if (i == 2) {
            return Button;
        }
        return null;
    }
}
