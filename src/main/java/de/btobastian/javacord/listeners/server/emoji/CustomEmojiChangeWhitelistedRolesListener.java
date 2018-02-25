package de.btobastian.javacord.listeners.server.emoji;

import de.btobastian.javacord.events.server.emoji.CustomEmojiChangeWhitelistedRolesEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji whitelisted roles changes.
 */
@FunctionalInterface
public interface CustomEmojiChangeWhitelistedRolesListener extends ServerAttachableListener,
        CustomEmojiAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji's whitelisted roles changed.
     *
     * @param event The event.
     */
    void onCustomEmojiChangeWhitelistedRoles(CustomEmojiChangeWhitelistedRolesEvent event);
}
