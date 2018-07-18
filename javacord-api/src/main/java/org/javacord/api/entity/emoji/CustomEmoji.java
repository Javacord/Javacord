package org.javacord.api.entity.emoji;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.UpdatableFromCache;

import java.util.Optional;

/**
 * This class represents a custom emoji.
 * If it's an unknown custom emoji, the object won't be unique and won't receive any updates!
 * Only {@link KnownCustomEmoji} receive updates!
 */
public interface CustomEmoji extends DiscordEntity, Nameable,  Emoji, UpdatableFromCache<KnownCustomEmoji> {

    /**
     * Gets the name of the emoji.
     *
     * @return The name of the emoji.
     */
    String getName();

    /**
     * Gets the image of the emoji.
     *
     * @return The image of the emoji.
     */
    Icon getImage();

    @Override
    default String getMentionTag() {
        return "<" + (isAnimated() ? "a" : "") + ":" + getName() + ":" + getIdAsString() + ">";
    }

    @Override
    default Optional<String> asUnicodeEmoji() {
        return Optional.empty();
    }

    @Override
    default Optional<CustomEmoji> asCustomEmoji() {
        return Optional.of(this);
    }

    @Override
    default Optional<KnownCustomEmoji> asKnownCustomEmoji() {
        return this instanceof KnownCustomEmoji ? Optional.of((KnownCustomEmoji) this) : Optional.empty();
    }

    @Override
    default Optional<KnownCustomEmoji> getCurrentCachedInstance() {
        return getApi().getCustomEmojiById(getId());
    }

}
