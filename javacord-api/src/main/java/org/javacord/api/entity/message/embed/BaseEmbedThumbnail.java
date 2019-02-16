package org.javacord.api.entity.message.embed;

import java.util.Optional;
import org.javacord.api.util.Specializable;

public interface BaseEmbedThumbnail extends Specializable<BaseEmbedThumbnail> {

    /**
     * Gets the url of the thumbnail.
     *
     * @return The url of the thumbnail.
     */
    Optional<String> getUrl();

}
