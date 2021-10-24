package org.javacord.api.entity.sticker;

/**
 * This enum represents the sticker format types.
 *
 * @see <a href="https://discord.com/developers/docs/resources/sticker#sticker-object-sticker-format-types">Discord Docs</a>
 */
public enum StickerFormatType {

    /**
     * A PNG (static) image.
     */
    PNG(1),

    /**
     * An APNG (animated) image.
     */
    APNG(2),

    /**
     * A LOTTIE json image.
     */
    LOTTIE(3),

    /**
     * An unknown image. Most likely something new added by Discord.
     */
    UNKNOWN(-1);

    /**
     * The ID of the sticker format type.
     */
    private final int id;

    /**
     * Creates a new sticker format type.
     *
     * @param id The ID of the sticker format type.
     */
    StickerFormatType(int id) {
        this.id = id;
    }

    /**
     * Parse an integer to a StickerFormatType.
     *
     * @param id The internal value of the sticker format type.
     * @return The StickerFormatType.
     */
    public static StickerFormatType fromId(int id) {
        for (StickerFormatType stickerFormatType : values()) {
            if (stickerFormatType.getId() == id) {
                return stickerFormatType;
            }
        }

        return UNKNOWN;
    }

    /**
     * Gets the internal value of the sticker's format type.
     *
     * @return The internal value of the sticker's format type.
     */
    public int getId() {
        return this.id;
    }
}
