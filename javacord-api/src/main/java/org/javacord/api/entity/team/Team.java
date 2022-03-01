package org.javacord.api.entity.team;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.user.User;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface Team extends DiscordEntity, Nameable {
    /**
     * Gets the icon of the team.
     *
     * @return The icon of the team.
     */
    Optional<Icon> getIcon();

    /**
     * Gets the members of the team.
     *
     * @return The members of the team.
     */
    Set<TeamMember> getTeamMembers();

    /**
     * Gets the id of the owner.
     *
     * @return The id of the owner.
     */
    long getOwnerId();

    /**
     * Gets the name of the team.
     *
     * @return The name of the team.
     */
    String getName();

    /**
     * Requests the owner of the team.
     *
     * @return The owner of the team.
     */
    default CompletableFuture<User> requestOwner() {
        return getApi().getUserById(getOwnerId());
    }
}
