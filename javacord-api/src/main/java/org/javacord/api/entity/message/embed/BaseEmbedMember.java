package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;

import java.util.Optional;

public interface BaseEmbedMember<E extends BaseEmbed, D extends EmbedDraftMember, S extends SentEmbedMember> {
    Optional<E> getEmbed();

    D toDraftMember();

    Optional<S> asSentEmbedMember();
}
