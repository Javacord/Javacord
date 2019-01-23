package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;

import java.net.URL;

public interface EmbedDraftFooter extends BaseEmbedFooter, EmbedDraftMember<EmbedDraftFooter, SentEmbedFooter> {
    EmbedDraftFooter setText(String text);

    EmbedDraftFooter setIconUrl(URL url);
}
