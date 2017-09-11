package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.MessageEditEvent;

/**
 * This listener listens to message edits.
 */
@FunctionalInterface
public interface MessageEditListener {

    /**
     * This method is called every time a message got edited.
     *
     * @param event The event.
     */
    void onMessageEdit(MessageEditEvent event);
}
