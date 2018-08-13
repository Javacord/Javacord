package org.javacord.core.entity.message.embed.parts.draft;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftImage;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of an image of an embed draft.
 */
public class EmbedDraftImageImpl implements EmbedDraftImage {
    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedDraftImageImpl.class);
    private final String imageUrl;
    private final FileContainer imageContainer;

    /**
     * Creates a new instance.
     *
     * @param imageUrl       A URL that points to an image.
     * @param imageContainer A FileContainer that contains an image file.
     */
    public EmbedDraftImageImpl(
            String imageUrl,
            FileContainer imageContainer) {
        this.imageUrl = imageUrl;
        this.imageContainer = imageContainer;
    }

    /**
     * Gets the image container.
     * This method is used internally for uploading the image to discord.
     *
     * @return The image container.
     */
    public FileContainer getImageContainer() {
        return imageContainer;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(imageUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
