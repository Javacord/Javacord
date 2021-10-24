package org.javacord.api.listener.server.sticker;

import org.javacord.api.event.server.sticker.StickerDeleteEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to the sticker delete event.
 */
@FunctionalInterface
public interface StickerDeleteListener extends ServerAttachableListener, StickerAttachableListener,
                                               GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a sticker is deleted.
     *
     * @param event The event.
     */
    void onStickerDelete(StickerDeleteEvent event);
}
