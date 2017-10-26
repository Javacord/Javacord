package de.btobastian.javacord.entities;

import java.util.Optional;

/**
 * Represents the information of an application (aka. bot).
 * This class won't get updated after being fetched!
 */
public interface ApplicationInfo {

    /**
     * Gets the client id of the application.
     *
     * @return The client id of the application.
     */
    long getClientId();

    /**
     * Gets the name of the application.
     *
     * @return The name of the application.
     */
    String getName();

    /**
     * Gets the description of the application.
     *
     * @return The description of the application.
     */
    String getDescription();

    /**
     * Check if the application's bot is public.
     *
     * @return Whether the application's bot is public or not.
     */
    boolean isPublicBot();

    /**
     * Check if the application's bot require OAuth2 code grant.
     *
     * @return Whether the application's bot require OAuth2 code grant or not.
     */
    boolean botRequiresCodeGrant();

    /**
     * Gets the name of the application owner.
     *
     * @return The name of the application owner.
     */
    String getOwnerName();

    /**
     * Gets the id of the application owner.
     *
     * @return The id of the application owner.
     */
    long getOwnerId();

    /**
     * Gets the discriminator of the application owner.
     *
     * @return The discriminator of the application owner.
     */
    String getOwnerDiscriminator();

    /**
     * Gets the owner of the application.
     * If you don't share a server with the owner of this application the Optional will be empty.
     *
     * @return The owner of the application.
     */
    Optional<User> getOwner();

}
