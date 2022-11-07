package org.javacord.api.listener.server.sticker;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.sticker.StickerDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to the sticker delete event.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_EMOJIS_AND_STICKERS})
public interface StickerDeleteListener extends ServerAttachableListener, StickerAttachableListener,
                                               GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a sticker is deleted.
     *
     * @param event The event.
     */
    void onStickerDelete(StickerDeleteEvent event);
}
