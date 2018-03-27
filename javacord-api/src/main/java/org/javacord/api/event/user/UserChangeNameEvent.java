package org.javacord.api.event.user;

/**
 * A user change name event.
 */
public interface UserChangeNameEvent extends UserEvent {

    /**
     * Gets the new name of the user.
     *
     * @return The new name of the user.
     */
    String getNewName();

    /**
     * Gets the old name of the user.
     *
     * @return The old name of the user.
     */
    String getOldName();

}
