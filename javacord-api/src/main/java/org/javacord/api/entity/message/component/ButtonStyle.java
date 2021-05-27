package org.javacord.api.entity.message.component;

public enum ButtonStyle {
    UNKNOWN(-1),
    PRIMARY(1),
    SECONDARY(2),
    SUCCESS(3),
    DANGER(4),
    LINK(5);

    private final int data;

    ButtonStyle(int i) {
        this.data = i;
    }

    /**
     * The button style internal value.
     *
     * @return The button style's internal value
     */
    public int getValue() {
        return this.data;
    }

    @Override
    public String toString() {
        return String.valueOf(this.data);
    }

    /**
     * Parse a color name into a ButtonStyle.
     *
     * @param name Button style name.
     * @return A ButtonStyle enumerator.
     */
    public static ButtonStyle fromName(String name) {
        switch (name) {
            case "blurple":
                return PRIMARY;
            case "grey":
                return SECONDARY;
            case "green":
                return SUCCESS;
            case "red":
                return DANGER;
            case "url":
                return LINK;
            default:
                return UNKNOWN;
        }
    }

    /**
     * Parse a color value into a ButtonStyle.
     *
     * @param colorId The style's internal value.
     * @return A ButtonStyle enumerator.
     */
    public static ButtonStyle fromId(int colorId) {
        switch (colorId) {
            case 1:
                return PRIMARY;
            case 2:
                return SECONDARY;
            case 3:
                return SUCCESS;
            case 4:
                return DANGER;
            case 5:
                return LINK;
            default:
                return UNKNOWN;
        }
    }
}
