package de.btobastian.javacord.entities.message.emoji;

import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.impl.ImplIcon;
import de.btobastian.javacord.utils.logging.LoggerUtil;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * This class represents a custom emoji.
 * If it's an unknown custom emoji, the object won't be unique and won't receive any updates!
 * Only {@link KnownCustomEmoji} receive updates!
 */
public interface CustomEmoji extends DiscordEntity, Emoji {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(CustomEmoji.class);

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
    default Icon getImage() {
        String urlString = "https://cdn.discordapp.com/emojis/" + getId() + (isAnimated() ? ".gif" : ".png");
        try {
            return new ImplIcon(getApi(), new URL(urlString));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the avatar is malformed! Please contact the developer!", e);
            return null;
        }
    }

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

    @Override
    default Optional<KnownCustomEmoji> asKnownCustomEmoji() {
        return this instanceof KnownCustomEmoji ? Optional.of((KnownCustomEmoji) this) : Optional.empty();
    }
}
