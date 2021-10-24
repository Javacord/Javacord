package org.javacord.api.entity.sticker;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.sticker.internal.StickerBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new stickers.
 */
public class StickerBuilder {

    /**
     * The delegate which is used by this instance.
     */
    private final StickerBuilderDelegate delegate;

    /**
     * Creates a new sticker builder.
     *
     * @param server The server that owns the sticker.
     */
    public StickerBuilder(Server server) {
        delegate = DelegateFactory.createStickerBuilderDelegate(server);
    }

    /**
     * Sets the name of the sticker. Has to be 2-30 characters long.
     *
     * @param name The name of the sticker.
     * @return The builder.
     */
    public StickerBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the sticker. Can be empty or 2-100 characters long.
     *
     * @param description The description of the sticker.
     * @return The builder.
     */
    public StickerBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the tags of the sticker. Can be up to 200 characters long.
     *
     * @param tags The tags of the sticker.
     * @return The builder.
     */
    public StickerBuilder setTags(String tags) {
        delegate.setTags(tags);
        return this;
    }

    /**
     * Sets the file to upload as sticker. Size cannot exceed 500 KB and file has to be type of
     * {@link StickerFormatType}
     *
     * @param file The file to upload as sticker.
     * @return The builder.
     */
    public StickerBuilder setFile(File file) {
        delegate.setFile(file);
        return this;
    }

    /**
     * Creates a new {@link Sticker} object with the given values.
     *
     * @return A future of the created sticker object.
     */
    public CompletableFuture<Sticker> create() {
        return delegate.create();
    }

    /**
     * Creates a new {@link Sticker} object with the given values.
     *
     * @param reason The reason for the audit log.
     * @return A future of the created sticker object.
     */
    public CompletableFuture<Sticker> create(String reason) {
        return delegate.create(reason);
    }

    /**
     * Gets the delegate of the sticker builder.
     *
     * @return The delegate of the sticker builder.
     */
    public StickerBuilderDelegate getDelegate() {
        return this.delegate;
    }
}
