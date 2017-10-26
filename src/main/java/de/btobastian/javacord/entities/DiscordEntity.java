package de.btobastian.javacord.entities;

import de.btobastian.javacord.DiscordApi;

import java.time.Instant;

/**
 * This class represents a Discord entity.
 */
public interface DiscordEntity {

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
     * @see <a href="https://discordapp.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    long getId();

    /**
     * Gets the id of the Discord entity as a string.
     *
     * @return The id of the Discord entity as a string.
     */
    default String getIdAsString() {
        return String.valueOf(getId());
    }

    /**
     * Gets the creation date of the Discord entity, calculated from the id.
     *
     * @return The creation date of the Discord entity.
     * @see <a href="https://discordapp.com/developers/docs/reference#snowflake-ids">Discord docs</a>
     */
    default Instant getCreationTimestamp() {
        // The first 42 bits (of the total 64) are the timestamp
        // Discord starts its counter at the first second of 2015
        return Instant.ofEpochMilli((getId() >> 22) + 1420070400000L);
    }

}
