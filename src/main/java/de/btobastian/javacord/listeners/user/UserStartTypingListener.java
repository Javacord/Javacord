package de.btobastian.javacord.listeners.user;


import de.btobastian.javacord.events.user.UserStartTypingEvent;

/**
 * This listener listens to users typing.
 */
@FunctionalInterface
public interface UserStartTypingListener {

    /**
     * This method is called every time a user starts typing.
     *
     * @param event The event.
     */
    void onUserStartTyping(UserStartTypingEvent event);
}
