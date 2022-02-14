package org.javacord.api.entity.message.component;

public enum TextInputStyle {

    UNKNOWN(-1),
    SHORT(1),
    PARAGRAPH(2);

    private final int data;

    TextInputStyle(int i) {
        this.data = i;
    }

    /**
     * The text input style internal value.
     *
     * @return The text input style's internal value
     */
    public int getValue() {
        return this.data;
    }

    @Override
    public String toString() {
        return String.valueOf(this.data);
    }

    /**
     * Gets a TextInputStyle by its id.
     *
     * @param style The style's internal value.
     * @return A TextInputStyle enumerator.
     */
    public static TextInputStyle fromId(int style) {
        for (TextInputStyle value : values()) {
            if (style == value.getValue()) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
