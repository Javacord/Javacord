package de.btobastian.javacord.entities.message.emoji;

import de.btobastian.javacord.entities.DiscordEntity;

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

    @Override
    default Optional<String> asUnicodeEmoji() {
        return Optional.empty();
    }

    @Override
    default Optional<CustomEmoji> asCustomEmoji() {
        return Optional.of(this);
    }

}
