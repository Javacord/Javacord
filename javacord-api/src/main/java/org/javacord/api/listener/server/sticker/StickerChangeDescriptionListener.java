package org.javacord.api.listener.server.sticker;

import org.javacord.api.event.server.sticker.StickerChangeDescriptionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to the sticker change description event.
 */
@FunctionalInterface
public interface StickerChangeDescriptionListener extends ServerAttachableListener, ObjectAttachableListener,
                                                          GloballyAttachableListener, StickerAttachableListener {

    /**
     * This method is called every time a sticker's description changed.
     *
     * @param event The event.
     */
    void onStickerChangeDescription(StickerChangeDescriptionEvent event);
}
