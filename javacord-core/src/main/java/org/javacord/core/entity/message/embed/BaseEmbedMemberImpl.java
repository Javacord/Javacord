package org.javacord.core.entity.message.embed;

import java.util.Optional;
import org.javacord.api.entity.message.embed.BaseEmbed;
import org.javacord.api.entity.message.embed.BaseEmbedAuthor;
import org.javacord.api.entity.message.embed.BaseEmbedFooter;
import org.javacord.api.entity.message.embed.BaseEmbedImage;
import org.javacord.api.entity.message.embed.BaseEmbedMember;
import org.javacord.api.entity.message.embed.BaseEmbedThumbnail;
import org.javacord.api.entity.message.embed.draft.EmbedDraftAuthor;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedAuthor;
import org.javacord.api.entity.message.embed.sent.SentEmbedFooter;
import org.javacord.api.entity.message.embed.sent.SentEmbedImage;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedThumbnail;
import org.javacord.core.entity.message.embed.draft.EmbedDraftAuthorImpl;
import org.javacord.core.entity.message.embed.draft.EmbedDraftFooterImpl;
import org.javacord.core.entity.message.embed.draft.EmbedDraftImageImpl;
import org.javacord.core.entity.message.embed.draft.EmbedDraftImpl;
import org.javacord.core.entity.message.embed.draft.EmbedDraftThumbnailImpl;

/**
 * Partial member class for embed field members; abstract implementation of {@link BaseEmbedMember}.
 *
 * @param <E> Generic-type of the parenting embed.
 * @param <D> Generic-type of the respective EmbedDraft member type.
 * @param <S> Generic-type of the respective SentEmbed member type.
 */
public abstract class BaseEmbedMemberImpl<E extends BaseEmbed, D extends EmbedDraftMember, S extends SentEmbedMember>
        implements BaseEmbedMember<E, D, S> {
    private final E parent;

    private final Class<D> dClass;
    private final Class<S> sClass;

    /**
     * Constructor.
     *
     * @param parent The parenting embed of this member.
     * @param dClass Type class of the respective EmbedDraft member type.
     * @param sClass Type class of the respective SentEmbed member type.
     */
    protected BaseEmbedMemberImpl(E parent, Class<D> dClass, Class<S> sClass) {
        this.parent = parent;

        this.dClass = dClass;
        this.sClass = sClass;
    }

    @Override
    public Optional<E> getEmbed() {
        return Optional.ofNullable(parent);
    }

    /**
     * {@inheritDoc}
     *
     * This method suppressed {@code unchecked} warnings because it is a compile-time false negative.
     */
    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    @Override
    public D toDraftMember() {
        if (dClass.isAssignableFrom(this.getClass())) {
            return (D) this;
        } else if (this instanceof SentEmbedAuthor) {
            return (D) new EmbedDraftAuthorImpl(parent, (BaseEmbedAuthor) this);
        } else if (this instanceof SentEmbedThumbnail) {
            return (D) new EmbedDraftThumbnailImpl(parent, (BaseEmbedThumbnail) this);
        } else if (this instanceof SentEmbedImage) {
            return (D) new EmbedDraftImageImpl(parent, (BaseEmbedImage) this);
        } else if (this instanceof SentEmbedFooter) {
            return (D) new EmbedDraftFooterImpl(parent, (BaseEmbedFooter) this);
        } else {
            throw new AssertionError();
        }
    }

    /**
     * {@inheritDoc}
     *
     * This method suppressed {@code unchecked} warnings because it is a compile-time false negative.
     */
    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    @Override
    public Optional<S> asSentEmbedMember() {
        if (sClass.isAssignableFrom(this.getClass())) {
            return Optional.of((S) this);
        }
        return Optional.empty();
    }
}
