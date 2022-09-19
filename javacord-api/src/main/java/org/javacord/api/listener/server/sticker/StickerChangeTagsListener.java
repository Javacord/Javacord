package org.javacord.api.listener.server.sticker;

import org.javacord.api.event.server.sticker.StickerChangeTagsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to the sticker change tags event.
 */
@FunctionalInterface
public interface StickerChangeTagsListener extends ServerAttachableListener, ObjectAttachableListener,
                                                   GloballyAttachableListener, StickerAttachableListener {

    /**
     * This method is called every time a sticker's tags changed.
     *
     * @param event The event.
     */
    void onStickerChangeTags(StickerChangeTagsEvent event);
}
