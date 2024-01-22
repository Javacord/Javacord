package org.javacord.api.entity;

public enum AttachmentFlag {
    /**
     * This attachment has been edited using the remix feature on mobile.
     */
    IS_REMIX(1 << 2),

    /**
     * The unknown message flag type.
     */
    UNKNOWN(-1);

    private final int id;

    /**
     * Creates a new attachment flags type.
     *
     * @param id attachment flags id.
     */
    AttachmentFlag(final int id) {
        this.id = id;
    }

    /**
     * Gets the id of the attachment flags type.
     *
     * @return The id of the attachment flags type.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the attashment flags type by its id.
     *
     * @param id The id of the attachment flag type.
     * @return The attachment flag type with the given id or {@link AttachmentFlag#UNKNOWN} if unknown id.
     */
    public static AttachmentFlag getFlagTypeById(final int id) {
        for (final AttachmentFlag value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return UNKNOWN;
    }

}
