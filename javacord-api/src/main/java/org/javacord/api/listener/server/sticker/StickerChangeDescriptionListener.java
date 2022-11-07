package org.javacord.api.listener.server.sticker;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.sticker.StickerChangeDescriptionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to the sticker change description event.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_EMOJIS_AND_STICKERS})
public interface StickerChangeDescriptionListener extends ServerAttachableListener, ObjectAttachableListener,
                                                          GloballyAttachableListener, StickerAttachableListener {

    /**
     * This method is called every time a sticker's description changed.
     *
     * @param event The event.
     */
    void onStickerChangeDescription(StickerChangeDescriptionEvent event);
}
