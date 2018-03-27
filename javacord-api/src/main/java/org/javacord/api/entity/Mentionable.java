package org.javacord.api.entity;

/**
 * This class represents an entity which is mentionable.
 */
public interface Mentionable {

    /**
     * Gets the tag used to mention the entity.
     *
     * @return The tag used to mention the entity.
     */
    String getMentionTag();

}
