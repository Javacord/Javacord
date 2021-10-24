package org.javacord.api.entity.sticker.internal;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.StickerUpdater;

import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link StickerUpdater}. You usually don't want to interact with this object.
 */
public interface StickerUpdaterDelegate {

    /**
     * Set the new name of the sticker.
     *
     * @param name The new name of the sticker.
     */
    void setName(String name);

    /**
     * Sets the new description of the sticker.
     *
     * @param description The new description of the sticker.
     */
    void setDescription(String description);

    /**
     * Sets the new tags of the sticker.
     *
     * @param tags The new tags of the sticker.
     */
    void setTags(String tags);

    /**
     * Updates the sticker with the given values.
     *
     * @return A future of the updated sticker object.
     */
    CompletableFuture<Sticker> update();

    /**
     * Updates the sticker with the given values.
     *
     * @param reason The reason for the audit log.
     * @return A future of the updated sticker object.
     */
    CompletableFuture<Sticker> update(String reason);
}
