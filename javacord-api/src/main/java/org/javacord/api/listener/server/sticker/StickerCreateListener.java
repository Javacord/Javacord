package org.javacord.api.listener.server.sticker;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.server.sticker.StickerCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to the sticker create event.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_EMOJIS_AND_STICKERS})
public interface StickerCreateListener extends ServerAttachableListener, GloballyAttachableListener,
                                               ObjectAttachableListener {

    /**
     * This method ist called every time a sticker is created.
     *
     * @param event The event.
     */
    void onStickerCreate(StickerCreateEvent event);
}
