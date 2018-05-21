package org.javacord.api.listener.server.emoji;

import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeWhitelistedRolesEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to custom emoji whitelisted roles changes.
 */
@FunctionalInterface
public interface KnownCustomEmojiChangeWhitelistedRolesListener extends ServerAttachableListener,
                                                                        KnownCustomEmojiAttachableListener,
                                                                        GloballyAttachableListener,
                                                                        ObjectAttachableListener {

    /**
     * This method is called every time a custom emoji's whitelisted roles changed.
     *
     * @param event The event.
     */
    void onKnownCustomEmojiChangeWhitelistedRoles(KnownCustomEmojiChangeWhitelistedRolesEvent event);
}
