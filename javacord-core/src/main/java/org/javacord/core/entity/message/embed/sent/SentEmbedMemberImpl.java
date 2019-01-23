package org.javacord.core.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.sent.SentEmbed;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;
import org.javacord.core.entity.message.embed.BaseEmbedMemberImpl;

public abstract class SentEmbedMemberImpl<D extends EmbedDraftMember, S extends SentEmbedMember>
        extends BaseEmbedMemberImpl<SentEmbed, D, S> implements SentEmbedMember<D, S> {

    protected SentEmbedMemberImpl(SentEmbed parent, Class<D> dClass, Class<S> sClass) {
        super(parent, dClass, sClass);
    }

}
