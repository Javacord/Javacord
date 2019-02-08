package org.javacord.api.entity.message.embed;

import java.net.URL;
import java.util.Optional;

import org.javacord.api.util.Specializable;

public interface BaseEmbedImage extends Specializable<BaseEmbedImage> {

    /**
     * Gets the url of the image.
     *
     * @return The url of the image.
     */
    Optional<String> getUrl();

}
