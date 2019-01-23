package org.javacord.core.entity.message.embed.draft;

import java.net.MalformedURLException;
import java.net.URL;
import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;
import org.javacord.core.entity.message.embed.BaseEmbedMemberImpl;

public abstract class EmbedDraftMemberImpl<D extends EmbedDraftMember, S extends SentEmbedMember>
        extends BaseEmbedMemberImpl<EmbedDraft, D, S> implements EmbedDraftMember<D, S> {

    protected EmbedDraftMemberImpl(EmbedDraft parent, Class<D> dClass, Class<S> sClass) {
        super(parent, dClass, sClass);
    }

    protected static URL urlOrNull(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ignored) {
            return null;
        }
    }
}
