package de.btobastian.javacord.entities.impl;

import de.btobastian.javacord.entities.Game;
import de.btobastian.javacord.entities.GameType;

import java.util.Objects;
import java.util.Optional;

/**
 * The implementation of {@link Game}.
 */
public class ImplGame implements Game {

    private final GameType type;
    private final String name;
    private final String streamingUrl;

    /**
     * Creates a new game object.
     *
     * @param type The type of the game.
     * @param name The name of the game.
     * @param streamingUrl The streaming url of the game. May be <code>null</code>.
     */
    public ImplGame(GameType type, String name, String streamingUrl) {
        this.type = type;
        this.name = name;
        this.streamingUrl = streamingUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getStreamingUrl() {
        return Optional.ofNullable(streamingUrl);
    }

    @Override
    public GameType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImplGame)) {
            return false;
        }
        ImplGame otherGame = (ImplGame) obj;
        return Objects.deepEquals(name, otherGame.name) && Objects.deepEquals(streamingUrl, otherGame.streamingUrl);
    }

    @Override
    public int hashCode() {
        int hash = 42;
        int typeHash = type.hashCode();
        int nameHash = name == null ? 0 : name.hashCode();
        int streamingUrlHash = streamingUrl == null ? 0 : streamingUrl.hashCode();

        hash = hash * 11 + typeHash;
        hash = hash * 17 + nameHash;
        hash = hash * 19 + streamingUrlHash;
        return hash;
    }
}
