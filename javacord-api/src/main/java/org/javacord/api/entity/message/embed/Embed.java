package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.embed.draft.EmbedDraft;
import org.javacord.api.entity.message.embed.sent.SentEmbed;

/**
 * This interface represents an embed.
 *
 * @deprecated Replaced by {@link BaseEmbed}, {@link SentEmbed} and {@link EmbedDraft}.
 */
@Deprecated
public interface Embed extends SentEmbed {

}