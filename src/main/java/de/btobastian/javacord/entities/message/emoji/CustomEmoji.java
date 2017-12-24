package de.btobastian.javacord.entities.message.emoji;

import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Server;

import java.util.Optional;

/**
 * This class represents a custom emoji.
 */
public interface CustomEmoji extends DiscordEntity, Emoji {

    /**
     * Gets the name of the emoji.
     *
     * @return The name of the emoji.
     */
    String getName();

    /**
     * Gets the server of the emoji.
     * We might not know the server of the emoji, if it was sent by a nitro user!
     *
     * @return The server of the emoji.
     */
    Optional<Server> getServer();

    @Override
    default String getMentionTag() {
        return "<" + (isAnimated() ? "a" : "") + ":" + getName() + ":" + getId() + ">";
    }

    @Override
    default Optional<String> asUnicodeEmoji() {
        return Optional.empty();
    }

    @Override
    default Optional<CustomEmoji> asCustomEmoji() {
        return Optional.of(this);
    }

}
