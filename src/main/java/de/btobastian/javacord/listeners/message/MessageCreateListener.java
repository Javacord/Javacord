package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageCreateEvent;

/**
 * This listener listens to message creations.
 */
@FunctionalInterface
public interface MessageCreateListener {

    /**
     * This method if called every time a message got created.
     *
     * @param event The event.
     */
    void onMessageCreate(MessageCreateEvent event);

}
