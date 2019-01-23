package org.javacord.core.entity.message.embed;

import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;
import org.javacord.core.entity.message.embed.draft.EmbedDraftImpl;

import java.util.Optional;

public abstract class BaseEmbedMemberImpl<E extends BaseEmbed, D extends EmbedDraftMember, S extends SentEmbedMember>
        implements BaseEmbedMember<E, D, S> {
    private final E parent;

    private final Class<D> dClass;
    private final Class<S> sClass;

    protected BaseEmbedMemberImpl(E parent, Class<D> dClass, Class<S> sClass) {
        this.parent = parent;

        this.dClass = dClass;
        this.sClass = sClass;
    }

    @Override
    public Optional<E> getEmbed() {
        return Optional.ofNullable(parent);
    }

    @Override
    public D toDraftMember() {
        if (dClass.isAssignableFrom(this.getClass())) {
            //noinspection unchecked -> False negative
            return (D) this;
        }
        return EmbedDraftImpl.createFrom(parent, (SentEmbedMember) this);
    }

    @Override
    public Optional<S> asSentEmbedMember() {
        if (sClass.isAssignableFrom(this.getClass())) {
            //noinspection unchecked -> False negative
            return Optional.of((S) this);
        }
        return Optional.empty();
    }
}
