package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;

public interface SentEmbedMember<D extends EmbedDraftMember, S extends SentEmbedMember>
        extends BaseEmbedMember<SentEmbed, D, S> {
}
