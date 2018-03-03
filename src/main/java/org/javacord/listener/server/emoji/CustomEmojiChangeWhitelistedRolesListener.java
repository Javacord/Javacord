package org.javacord.listener.server.emoji;

import org.javacord.event.server.emoji.CustomEmojiChangeWhitelistedRolesEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.server.emoji.CustomEmojiChangeWhitelistedRolesEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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
