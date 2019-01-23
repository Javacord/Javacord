package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;

import java.util.Optional;

public interface MissingEmbedDraftMember<S extends SentEmbedMember> extends SentEmbedMember<EmbedDraftMember, S> {
    @Override
    default EmbedDraftMember toDraftMember() {
        throw new UnsupportedOperationException("This SentEmbedMember cannot be converted to an EmbedDraftMember!");
    }

    @Override
    default Optional<S> asSentEmbedMember() {
        //noinspection unchecked -> False positive
        return Optional.of((S) this);
    }
}
