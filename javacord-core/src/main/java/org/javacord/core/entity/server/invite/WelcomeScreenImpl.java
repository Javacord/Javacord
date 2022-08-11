package org.javacord.core.entity.server.invite;

import com.fasterxml.jackson.databind.JsonNode;

import org.javacord.api.entity.server.invite.WelcomeScreen;
import org.javacord.api.entity.server.invite.WelcomeScreenChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of {@link WelcomeScreen}.
 */
public class WelcomeScreenImpl implements WelcomeScreen {

    /**
     * The server description shown in the welcome screen.
     */
    private final String description;

    /**
     * The channels shown in the welcome screen, up to 5.
     */
    private final List<WelcomeScreenChannel> welcomeScreenChannels = new ArrayList<>();

    /**
     * Creates a new Welcome Screen Object.
     *
     * @param data The json data of the welcome screen.
     */
    public WelcomeScreenImpl(JsonNode data) {
        this.description = data.get("description").asText();
        for (JsonNode welcomeChannel : data.get("welcome_channels")) {
            this.welcomeScreenChannels.add(new WelcomeScreenChannelImpl(welcomeChannel));
        }
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public List<WelcomeScreenChannel> getWelcomeScreenChannels() {
        return Collections.unmodifiableList(welcomeScreenChannels);
    }
}
