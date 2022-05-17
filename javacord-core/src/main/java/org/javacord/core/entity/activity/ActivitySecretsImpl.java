package org.javacord.core.entity.activity;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.activity.ActivitySecrets;

import java.util.Optional;

public class ActivitySecretsImpl implements ActivitySecrets {
    private final String join;

    private final String spectate;

    private final String match;

    /**
     * Creates a new activity secrets object.
     * 
     * @param data The json data of the activity.
     */
    public ActivitySecretsImpl(JsonNode data) {
        this.join = data.has("join") ? data.get("join").asText() : null;
        this.spectate = data.has("spectate") ? data.get("spectate").asText() : null;
        this.match = data.has("match") ? data.get("match").asText() : null;
    }

    @Override
    public Optional<String> getJoin() {
        return Optional.ofNullable(join);
    }

    @Override
    public Optional<String> getSpectate() {
        return Optional.ofNullable(spectate);
    }

    @Override
    public Optional<String> getMatch() {
        return Optional.ofNullable(match);
    }
}
