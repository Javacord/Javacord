package org.javacord.api.entity.message.embed.sent;

import org.javacord.api.entity.Nameable;

public interface SentEmbedProvider extends Nameable, MissingEmbedDraftMember<SentEmbedProvider> {

    /**
     * Gets the url of the provider.
     *
     * @return The url of the provider.
     */
    String getUrl();

}
