package org.javacord.api.entity.message.embed;

import java.net.URL;
import java.util.Optional;

import org.javacord.api.util.Specializable;

public interface BaseEmbedFooter extends Specializable<BaseEmbedFooter> {

    /**
     * Gets the footer text.
     *
     * @return The text of the footer.
     */
    String getText();

    /**
     * Gets the url of the footer icon.
     *
     * @return The url of the footer icon.
     */
    Optional<URL> getIconUrl();

}
