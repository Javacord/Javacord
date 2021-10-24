package org.javacord.api.entity.sticker;

/**
 * This class represents the sticker types.
 */
public enum StickerType {

    /**
     * This sticker is a default sticker by Discord.
     */
    STANDARD(1),

    /**
     * This sticker is uploaded to a server.
     */
    SERVER(2),

    /**
     * An unknown sticker type, most likely something new added by Discord.
     */
    UNKNOWN(-1);

    /**
     * The ID of the sticker type.
     */
    private final int id;

    /**
     * Creates a new sticker type.
     *
     * @param id The ID of the sticker type.
     */
    StickerType(int id) {
        this.id = id;
    }

    /**
     * Parse an integer to a StickerType.
     *
     * @param id The internal id of the sticker type.
     * @return The StickerType.
     */
    public static StickerType fromId(int id) {
        for (StickerType stickerType : values()) {
            if (stickerType.getId() == id) {
                return stickerType;
            }
        }

        return UNKNOWN;
    }

    /**
     * Gets the internal value of the sticker's type.
     *
     * @return The internal value of the sticker's type.
     */
    public int getId() {
        return this.id;
    }
}
