package org.javacord.api.entity;

import org.javacord.api.entity.team.Team;
import org.javacord.api.entity.user.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
     * Gets the name of the application owner.
     *
     * <p>If the application is owned by a user, this will be the username. If it
     * is owned by a team, the return value will be empty.</p>
     *
     * @return The name of the application owner, if applicable.
     * @deprecated This method is deprecated. Access {@link #getOwner()} instead. This method is scheduled for removal.
     */
    @Deprecated
    default Optional<String> getOwnerName() {
        return getOwner().map(ApplicationOwner::getName);
    }

    /**
     * Gets the id of the application owner.
     *
     * @return The id of the application owner.
     * @deprecated This method is deprecated. Access {@link #getOwner()} instead. This method is scheduled for removal.
     */
    @Deprecated
    default Optional<Long> getOwnerId() {
        return getOwner().isPresent() ? Optional.of(getOwner().get().getId()) : Optional.empty();
    }

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
     * Gets the discriminator of the application owner.
     *
     * <p>The contents of this are undefined if the owner is a team, but will
     * likely always be "0000"</p>
     *
     * @return The discriminator of the application owner, if applicable.
     * @deprecated This method is deprecated. Access {@link #getOwner()} instead. This method is scheduled for removal.
     */
    @Deprecated
    default Optional<String> getOwnerDiscriminator() {
        return getOwner().map(ApplicationOwner::getDiscriminator);
    }

    /**
     * Requests the owner of the application.
     *
     * @return The owner of the application.
     * @deprecated This method is deprecated. Access {@link #getOwner()} instead. This method is scheduled for removal.
     */
    @Deprecated
    default CompletableFuture<Optional<User>> requestOwner() {
        if (getOwner().isPresent()) {
            return getOwner().get().requestUser().thenApply(Optional::of);
        } else {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }

    /**
     * Gets the team owning the application.
     *
     * @return The team owning the application, if applicable.
     */
    Optional<Team> getTeam();

}
