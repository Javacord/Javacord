package org.javacord.api.entity.server;

import java.util.Arrays;

public enum PrivacyLevel {
    UNKNOWN(-1),
    GUILD_ONLY(2);

    /**
     * The id of the Privacy level.
     */
    private final int id;

    /**
     * Creates a new Privacy level.
     *
     * @param id The id of the Privacy level.
     */
    PrivacyLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the Privacy level.
     *
     * @return The id  of the Privacy level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the Privacy level by id.
     * @param id The id of the Privacy level.
     * @return The Privacy level with the given id.
     */
    public static PrivacyLevel fromId(int id) {
        return Arrays.stream(values())
                .filter(privacyLevel -> privacyLevel.getId() == id)
                .findFirst().orElse(UNKNOWN);
    }
}
