package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageDeleteEvent;

/**
 * This listener listens to message deletions.
 */
@FunctionalInterface
public interface MessageDeleteListener {

    /**
     * This method is called every time a message got deleted.
     *
     * @param event The event.
     */
    void onMessageDelete(MessageDeleteEvent event);
}
