package org.javacord.core.entity.message.embed.parts.draft;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.parts.draft.EmbedDraftFooter;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * The implementation of a footer for an embed draft.
 */
public class EmbedDraftFooterImpl implements EmbedDraftFooter {
    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(EmbedDraftFooterImpl.class);
    private final String footerText;
    private final String footerIconUrl;
    private final FileContainer footerIconContainer;

    /**
     * Creates a new instance.
     *
     * @param footerText          A footer text.
     * @param footerIconUrl       An URL to an icon for the footer.
     * @param footerIconContainer A FileContainer that contains the icon file.
     */
    public EmbedDraftFooterImpl(
            String footerText,
            String footerIconUrl,
            FileContainer footerIconContainer) {
        this.footerText = footerText;
        this.footerIconUrl = footerIconUrl;
        this.footerIconContainer = footerIconContainer;
    }

    /**
     * Gets the icon container.
     * This method is used internally for uploading the icon file to discord.
     *
     * @return The icon container.
     */
    public FileContainer getFooterIconContainer() {
        return footerIconContainer;
    }

    @Override
    public Optional<String> getText() {
        return Optional.ofNullable(footerText);
    }

    @Override
    public Optional<URL> getIconUrl() {
        try {
            return Optional.of(new URL(footerIconUrl));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }
}
