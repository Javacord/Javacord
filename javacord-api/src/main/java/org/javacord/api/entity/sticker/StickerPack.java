package org.javacord.api.entity.sticker;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Nameable;

import java.util.Optional;
import java.util.Set;

/**
 * This class represents a sticker pack.
 *
 * @see <a href="https://discord.com/developers/docs/resources/sticker#sticker-pack-object">Discord Docs</a>
 */
public interface StickerPack extends DiscordEntity, Nameable {

    /**
     * Gets the stickers in this pack.
     *
     * @return The stickers in this pack.
     */
    Set<Sticker> getStickers();

    /**
     * Gets the SKU ID of the sticker pack.
     *
     * @return The SKU ID of the sticker pack.
     */
    long getSkuId();

    /**
     * Gets the ID of the sticker that is shown as the pack's cover.
     *
     * @return The ID of the sticker that is shown as the pack's cover.
     */
    Optional<Long> getCoverStickerId();

    /**
     * Gets the description of the sticker pack.
     *
     * @return The description of the sticker pack.
     */
    String getDescription();

    /**
     * Gets the ID of the sticker pack's banner image.
     *
     * @return The ID of the sticker pack's banner image.
     */
    long getBannerAssetId();
}
