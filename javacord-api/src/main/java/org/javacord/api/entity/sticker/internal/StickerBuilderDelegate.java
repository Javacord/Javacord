package org.javacord.api.entity.sticker.internal;

import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.StickerBuilder;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link StickerBuilder}. You usually don't want to interact with this object.
 */
public interface StickerBuilderDelegate {

    /**
     * Copies the values of the sticker into the builder.
     *
     * @param sticker The sticker to copy.
     */
    void copy(Sticker sticker);

    /**
     * Sets the name of the sticker.
     *
     * @param name The name of the sticker.
     */
    void setName(String name);

    /**
     * Sets the description of the sticker.
     *
     * @param description The description of the sticker.
     */
    void setDescription(String description);

    /**
     * Sets the tags of the sticker.
     *
     * @param tags The tags of the sticker.
     */
    void setTags(String tags);

    /**
     * Sets the file to upload as sticker.
     *
     * @param file The file to upload as sticker.
     */
    void setFile(File file);

    /**
     * Creates a new {@link Sticker} object with the given values.
     *
     * @return A future of the created sticker object.
     */
    CompletableFuture<Sticker> create();

    /**
     * Creates a new {@link Sticker} object with the given values.
     *
     * @param reason The reason for the audit log.
     * @return A future of the created sticker object.
     */
    CompletableFuture<Sticker> create(String reason);
}
