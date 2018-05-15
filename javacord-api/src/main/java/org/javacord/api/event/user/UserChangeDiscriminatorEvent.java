package org.javacord.api.event.user;

/**
 * A user change discriminator event.
 */
public interface UserChangeDiscriminatorEvent extends UserEvent {

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
