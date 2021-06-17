package org.javacord.api.entity.server;

import java.util.Arrays;

/**
 * An enum containing all the NSFW Levels a server can have.
 */
public enum NsfwLevel {

    /**
     * The server has not set a specific NSFW level and
     * is considered as neutral.
     */
    DEFAULT(0),

    /**
     * The server is considered explicit.
     */
    EXPLICIT(1),

    /**
     * The server is safe for work.
     */
    SAFE(2),

    /**
     * The server is age restricted.
     */
    AGE_RESTRICTED(3),

    /**
     * An unknown NSFW level, most likely something new added by Discord.
     */
    UNKNOWN(-1);

    /**
     * The id of the NSFW level.
     */
    private final int id;

    /**
     * Creates a new NSFW level.
     *
     * @param id The id of the NSFW level.
     */
    NsfwLevel(int id) {
        this.id = id;
    }

    /**
     * Gets the id of the NSFW level.
     *
     * @return The id  of the NSFW level.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the NSFW level by id.
     * @param id The id of the NSFW level.
     * @return The NSFW level with the given id.
     */
    public static NsfwLevel fromId(int id) {
        return Arrays.stream(values())
                .filter(nsfwLevel -> nsfwLevel.getId() == id)
                .findFirst().orElse(UNKNOWN);
    }

}
