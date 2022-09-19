package org.javacord.api.entity.sticker;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Nameable;

/**
 * This class represents the sticker item object.
 *
 * @see <a href="https://discord.com/developers/docs/resources/sticker#sticker-item-object">Discord Docs</a>
 */
public interface StickerItem extends DiscordEntity, Nameable {

    /**
     * Gets the format type of the sticker.
     *
     * @return The format type of the sticker.
     */
    StickerFormatType getFormatType();
}
