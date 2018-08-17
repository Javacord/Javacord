package org.javacord.core.entity.message.embed.parts.draft;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftThumbnail;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The implementation of a thumbnail for an embed draft.
 */
public class EmbedDraftThumbnailImpl implements EmbedDraftThumbnail {
    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedDraftThumbnailImpl.class);
    private final String thumbnailUrl;
    private final FileContainer thumbnailContainer;

    /**
     * Creates a new instance.
     *
     * @param thumbnailUrl       An URL that points to the thumbnail image.
     * @param thumbnailContainer A FileContainer to contain the thumbnail file.
     */
    public EmbedDraftThumbnailImpl(
            String thumbnailUrl,
            FileContainer thumbnailContainer) {
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailContainer = thumbnailContainer;
    }

    /**
     * Gets the thumbnail container.
     * This method is used internally to upload the thumbnail file to discord.
     *
     * @return The thumbnail container.
     */
    public FileContainer getThumbnailContainer() {
        return thumbnailContainer;
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(thumbnailUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
