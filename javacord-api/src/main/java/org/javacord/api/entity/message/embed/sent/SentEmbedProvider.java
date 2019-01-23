package org.javacord.api.entity.message.embed.sent;

import java.net.URL;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.embed.draft.EmbedDraftMember;

public interface SentEmbedProvider extends Nameable, MissingEmbedDraftMember<SentEmbedProvider> {

    /**
     * Gets the url of the provider.
     *
     * @return The url of the provider.
     */
    URL getUrl();

}
