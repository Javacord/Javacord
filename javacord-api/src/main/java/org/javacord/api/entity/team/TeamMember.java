package org.javacord.api.entity.team;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.user.User;

import java.util.concurrent.CompletableFuture;

public interface TeamMember extends DiscordEntity {

    /**
     * Gets the state of the membership.
     *
     * @return The state of the membership.
     */
    TeamMembershipState getMembershipState();

    /**
     * Gets the user id of this member.
     *
     * @return The user id.
     */
    @Override
    long getId();

    /**
     * Requests the member as user.
     *
     * <p>If the user is not cached, it will be requested.</p>
     *
     * @return The member as user.
     */
    default CompletableFuture<User> requestUser() {
        return getApi().getUserById(getId());
    }
}
