package org.javacord.api.entity;

import org.javacord.api.entity.team.Team;

import java.util.Optional;

/**
 * Represents the information of an application (aka. bot).
 * This class won't get updated after being fetched!
 */
public interface ApplicationInfo extends Nameable {

    /**
     * Gets the client id of the application.
     *
     * @return The client id of the application.
     */
    long getClientId();

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
     * Check if the application is owned by a team rather than a single user.
     *
     * @return Whether the application is owned by a team.
     */
    default boolean isOwnedByTeam() {
        return getTeam().isPresent();
    }

    /**
     * Check if the application's bot require OAuth2 code grant.
     *
     * @return Whether the application's bot require OAuth2 code grant or not.
     */
    boolean botRequiresCodeGrant();

    /**
     * Gets the owner of the Application.
     *
     * <p>An application might be owned by a user or team. If it is not owned
     * by a user, this methods return value will be empty.</p>
     *
     * @return The user owning this application, if applicable.
     */
    Optional<ApplicationOwner> getOwner();

    /**
     * Gets the team owning the application.
     *
     * @return The team owning the application, if applicable.
     */
    Optional<Team> getTeam();

}
