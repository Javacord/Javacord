package de.btobastian.javacord.listener.server.emoji;

import de.btobastian.javacord.event.server.emoji.CustomEmojiChangeWhitelistedRolesEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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
