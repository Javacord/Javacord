package org.javacord.api.listener.server.emoji;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.emoji.KnownCustomEmojiChangeWhitelistedRolesEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to custom emoji whitelisted roles changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_EMOJIS_AND_STICKERS})
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
