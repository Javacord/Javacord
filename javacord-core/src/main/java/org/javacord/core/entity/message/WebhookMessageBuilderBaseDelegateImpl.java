package org.javacord.core.entity.message;

import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.WebhookMessageBuilderBaseDelegate;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Arrays;

/**
 * The implementation of {@link WebhookMessageBuilderBaseDelegate}.
 */
public class WebhookMessageBuilderBaseDelegateImpl extends MessageBuilderDelegateImpl
        implements WebhookMessageBuilderBaseDelegate {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(WebhookMessageBuilderBaseDelegateImpl.class);

    @Override
    public void addEmbeds(EmbedBuilder... embeds) {
        this.embeds.addAll(Arrays.asList(embeds));
    }

    @Override
    public void removeEmbed(EmbedBuilder embed) {
        this.embeds.remove(embed);
    }

    @Override
    public void removeEmbeds(EmbedBuilder... embeds) {
        this.embeds.removeAll(Arrays.asList(embeds));
    }

}
