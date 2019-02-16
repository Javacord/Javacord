package org.javacord.api.entity.message.embed;

import java.util.Optional;
import org.javacord.api.entity.Nameable;
import org.javacord.api.util.Specializable;

public interface BaseEmbedAuthor extends Nameable, Specializable<BaseEmbedAuthor> {

    /**
     * Gets the url of the author.
     *
     * @return The url of the author.
     */
    Optional<String> getUrl();

    /**
     * Gets the url of the author icon.
     *
     * @return The url of the author icon.
     */
    Optional<String> getIconUrl();

}
