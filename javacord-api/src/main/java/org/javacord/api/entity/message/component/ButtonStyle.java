package org.javacord.api.entity.message.component;

public enum ButtonStyle {
    Primary(1),
    Secondary(2),
    Success(3),
    Danger(4),
    Link(5),
    ;

    private final int data;

    ButtonStyle(int i) {
        this.data = i;
    }

    public int value() {
        return this.data;
    }

    public static ButtonStyle devalue(String name) {
        switch (name) {
            case "blurple":
                return Primary;
            case "grey":
                return Secondary;
            case "green":
                return Success;
            case "red":
                return Danger;
            case "url":
                return Link;
            default:
                return null;
        }
    }

    public static ButtonStyle devalue(int i) {
        switch (i) {
            case 1:
                return Primary;
            case 2:
                return Secondary;
            case 3:
                return Success;
            case 4:
                return Danger;
            case 5:
                return Link;
            default:
                return null;
        }
    }
}
