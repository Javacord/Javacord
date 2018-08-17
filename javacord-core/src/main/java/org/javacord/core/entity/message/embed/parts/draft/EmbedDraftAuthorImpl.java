package org.javacord.core.entity.message.embed.parts.draft;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftAuthor;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation for an author for an embed draft.
 */
public class EmbedDraftAuthorImpl implements EmbedDraftAuthor {
    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedDraftAuthorImpl.class);
    private final String authorName;
    private final String authorUrl;
    private final String authorIconUrl;
    private final FileContainer authorIconContainer;

    /**
     * Creates a new instance.
     *
     * @param authorName          The name of the author.
     * @param authorUrl           The URL for the author.
     * @param authorIconUrl       The URL of an icon to be shown.
     * @param authorIconContainer A FileContainer to contain an icon to be shown.
     */
    public EmbedDraftAuthorImpl(
            String authorName,
            String authorUrl,
            String authorIconUrl,
            FileContainer authorIconContainer) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.authorIconUrl = authorIconUrl;
        this.authorIconContainer = authorIconContainer;
    }

    /**
     * Gets the icon container.
     * This method is used internally to upload the icon file to discord.
     *
     * @return the icon container.
     */
    public FileContainer getAuthorIconContainer() {
        return authorIconContainer;
    }

    @Override
    public Optional<URL> getUrl() {
        try {
            return Optional.of(new URL(authorUrl));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<URL> getIconUrl() {
        try {
            return Optional.of(new URL(authorIconUrl));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return authorName;
    }
}
