package org.javacord.api.entity;

import org.javacord.api.DiscordApi;

import java.time.Instant;

/**
 * This class represents a Discord entity.
 */
public interface DiscordEntity {

    /**
     * Calculates the creation date from the given Discord entity id.
     *
     * @param entityId The entity it to calculate from.
     * @return The creation date of the Discord entity.
     * @see <a href="https://discord.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    static Instant getCreationTimestamp(long entityId) {
        // The first 42 bits (of the total 64) are the timestamp
        // Discord starts its counter at the first second of 2015
        return Instant.ofEpochMilli((entityId >>> 22) + 1420070400000L);
    }

    /**
     * Gets the creation date of the Discord entity, calculated from the id.
     *
     * @return The creation date of the Discord entity.
     * @see <a href="https://discord.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    default Instant getCreationTimestamp() {
        return getCreationTimestamp(getId());
    }

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    DiscordApi getApi();

    /**
     * Gets the id of Discord entity.
     *
     * @return The id of Discord entity.
     * @see <a href="https://discord.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    long getId();

    /**
     * Gets the id of the Discord entity as a string.
     *
     * @return The id of the Discord entity as a string.
     */
    default String getIdAsString() {
        return Long.toUnsignedString(getId());
    }


}
