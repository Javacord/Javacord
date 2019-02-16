package org.javacord.api.entity.message.embed;

import java.util.Optional;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;
import org.javacord.api.entity.message.embed.sent.SentEmbedMember;

public interface BaseEmbedMember<E extends BaseEmbed, D extends EmbedDraftMember, S extends SentEmbedMember> {
    Optional<E> getEmbed();

    D toDraftMember();

    Optional<S> asSentEmbedMember();
}
