package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeAvatarEvent;

/**
 * This listener listens to user avatar changes.
 */
@FunctionalInterface
public interface UserChangeAvatarListener {

    /**
     * This method is called every time a user changed their avatar.
     *
     * @param event The event.
     */
    void onUserChangeAvatar(UserChangeAvatarEvent event);

}