package org.javacord.api.entity;

public enum AttachmentFlags {
    /**
     * This attachment has been edited using the remix feature on mobile.
     */
    IS_REMIX(1 << 2),

    /**
     * The unknown message flag type.
     */
    UNKNOWN(-1);

    private final int bitfield;

    /**
     * Creates a new attachment flags type.
     *
     * @param bitfield attachment flags combined as a bitfield.
     */
    AttachmentFlags(final int bitfield) {
        this.bitfield = bitfield;
    }

    /**
     * Gets the bitfield of the attachment flags type.
     *
     * @return The bitfield of the attachment flags type.
     */
    public int getBitfield() {
        return this.bitfield;
    }

    /**
     * Gets the attashment flags type by its bitfield.
     *
     * @param bitfield The bitfield of the attachment flag type.
     * @return The attachment flag type with the given id or {@link AttachmentFlags#UNKNOWN} if unknown bitfield.
     */
    public static AttachmentFlags getFlagTypeByBitfield(final int bitfield) {
        for (final AttachmentFlags value : values()) {
            if (value.getBitfield() == bitfield) {
                return value;
            }
        }
        return UNKNOWN;
    }

}
