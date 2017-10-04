package de.btobastian.javacord.listeners.user;

import de.btobastian.javacord.events.user.UserChangeNicknameEvent;

/**
 * This listener listens to user nickname changes.
 */
@FunctionalInterface
public interface UserChangeNicknameListener {

    /**
     * This method is called every time a user changed their nickname on a server.
     *
     * @param event The event.
     */
    void onUserChangeNickname(UserChangeNicknameEvent event);
}
