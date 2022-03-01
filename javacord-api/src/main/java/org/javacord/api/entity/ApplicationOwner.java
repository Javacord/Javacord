package org.javacord.api.entity;

import org.javacord.api.entity.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * The owner of an application.
 *
 * @see ApplicationInfo
 * @see org.javacord.api.entity.team.Team
 */
public interface ApplicationOwner extends DiscordEntity, Nameable {

    /**
     * The owner's name without discriminator.
     *
     * @return The owner's name.
     */
    @Override
    String getName();

    /**
     * The owner's discriminator.
     *
     * @return The owner's discriminator.
     */
    String getDiscriminator();

    /**
     * The owner's discriminated name.
     *
     * @return The owner's discriminated name.
     */
    default String getDiscriminatedName() {
        return getName() + "#" + getDiscriminator();
    }

    /**
     * Requests the owner as a User.
     *
     * @return The owner as a User.
     */
    CompletableFuture<User> requestUser();

}
