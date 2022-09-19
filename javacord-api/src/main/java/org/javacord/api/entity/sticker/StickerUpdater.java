package org.javacord.api.entity.sticker;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.sticker.internal.StickerUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.concurrent.CompletableFuture;

/**
 * This class is used to update stickers.
 */
public class StickerUpdater {

    /**
     * The delegate which is used by this instance.
     */
    private final StickerUpdaterDelegate delegate;

    /**
     * Creates a new instance of the sticker updater.
     *
     * @param server The server that owns the sticker.
     * @param id     The ID of the sticker.
     */
    public StickerUpdater(Server server, long id) {
        delegate = DelegateFactory.createStickerUpdaterDelegate(server, id);
    }

    /**
     * Updates the name of the sticker.
     *
     * @param name The new name of the sticker.
     * @return The builder.
     */
    public StickerUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Updates the description of the sticker.
     *
     * @param description The new description of the sticker.
     * @return The builder.
     */
    public StickerUpdater setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Updates the tags of the sticker.
     *
     * @param tags The new tags of the sticker.
     * @return The builder.
     */
    public StickerUpdater setTags(String tags) {
        delegate.setTags(tags);
        return this;
    }

    /**
     * Updates the sticker.
     *
     * @return The updated sticker.
     */
    public CompletableFuture<Sticker> update() {
        return delegate.update();
    }

    /**
     * Updates the sticker.
     *
     * @param reason The reason for the audit log.
     * @return The updated sticker.
     */
    public CompletableFuture<Sticker> update(String reason) {
        return delegate.update(reason);
    }

    /**
     * Gets the delegate of the sticker updater.
     *
     * @return The delegate of the sticker updater.
     */
    public StickerUpdaterDelegate getDelegate() {
        return this.delegate;
    }
}
