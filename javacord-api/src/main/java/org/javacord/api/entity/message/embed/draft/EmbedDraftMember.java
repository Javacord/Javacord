package org.javacord.api.entity.message.embed.draft;

import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;

public interface EmbedDraftMember<D extends EmbedDraftMember, S extends SentEmbedMember>
        extends BaseEmbedMember<EmbedDraft, D, S> {
}
