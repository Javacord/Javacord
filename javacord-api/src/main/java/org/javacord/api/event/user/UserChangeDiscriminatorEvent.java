package org.javacord.api.event.user;

import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change discriminator event.
 */
public interface UserChangeDiscriminatorEvent extends ServerMemberEvent {

    /**
     * Gets the new discriminator of the user.
     *
     * @return The new discriminator of the user.
     */
    String getNewDiscriminator();

    /**
     * Gets the old discriminator of the user.
     *
     * @return The old discriminator of the user.
     */
    String getOldDiscriminator();

}
