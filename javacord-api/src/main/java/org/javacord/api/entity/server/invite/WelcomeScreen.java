package org.javacord.api.entity.server.invite;

import java.util.List;
import java.util.Optional;

/**
 * This class represents a Welcome Screen.
 */
public interface WelcomeScreen {

    /**
     * Gets the server description shown in the welcome screen.
     * 
     * @return the server description shown in the welcome screen.
     */
    Optional<String>  getDescription();

    /**
     * Gets the channels shown in the welcome screen.
     * 
     * @return the channels shown in the welcome screen.
     */
    List<WelcomeScreenChannel> getWelcomeScreenChannels();
}
