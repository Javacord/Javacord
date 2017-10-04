package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageCreateEvent;

/**
 * This listener listens to message creations.
 */
@FunctionalInterface
public interface MessageCreateListener {

    /**
     * This method is called every time a message is created.
     *
     * @param event The event.
     */
    void onMessageCreate(MessageCreateEvent event);

}
