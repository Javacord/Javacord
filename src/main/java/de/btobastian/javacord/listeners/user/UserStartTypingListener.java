package de.btobastian.javacord.listeners.user;


import de.btobastian.javacord.events.user.UserStartTypingEvent;

/**
 * This listener listens to users typing.
 * If the user starts typing the "xyz is typing..." message is displayed for 5 seconds.
 * It also stops if the user sent a message.
 */
@FunctionalInterface
public interface UserStartTypingListener {

    /**
     * This method is called every time a user started typing.
     *
     * @param event The event.
     */
    void onUserStartTyping(UserStartTypingEvent event);
}
